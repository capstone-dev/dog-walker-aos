package ajou.ac.kr.teaming.activity.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ajou.ac.kr.teaming.R;

public class CameraMainFragment extends Fragment {

    private static final String TAG = "CameraMainFragment";

    private TextureView textureView;

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;

    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;

    private Handler backgroudHandler;
    private HandlerThread backgroudThread;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /** Life Cycle */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_main, container, false);

        textureView = view.findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(textureListener);

        view.findViewById
                (R.id.btn_tackPicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //savePhoto();
                takePhoto();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume");

        startBackgourdThread();

        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");

        //closeCamera();
        stopBackgroudThread();

        super.onPause();
    }

    public void takePhoto() {
        Bitmap bitmap = textureView.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] bytes = stream.toByteArray();

        Intent intent = new Intent(getActivity(), CameraPhotoActivity.class);
        intent.putExtra("imageBytes", bytes);
        startActivity(intent); //이미지를
    }

    public void savePhoto() {
        ImageReader reader = ImageReader.newInstance(textureView.getBitmap().getWidth(), textureView.getBitmap().getHeight(), ImageFormat.JPEG, 1);

        List<Surface> outputSurfaces = new ArrayList<>(2);
        outputSurfaces.add(reader.getSurface());
        outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

        try {
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            final File file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
            reader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);

                    save(bytes);
                }

                private void save(byte[] bytes) {
                    try (OutputStream outputStream = new FileOutputStream(file)) {
                        outputStream.write(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, backgroudHandler);


            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureCallback, backgroudHandler);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, backgroudHandler);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "camera open");

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            cameraManager.openCamera(cameraId, cameraDeviceStateCallback, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }

        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
    }

    private void createCameraPreview() {
        SurfaceTexture texture = textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());

        try {
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraDevice == null) {
                        return;
                    }

                    cameraCaptureSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(getActivity(), "onConfigureFailed", Toast.LENGTH_SHORT).show();
                }

            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void updatePreview() {
        if (cameraDevice == null) {
            Log.e(TAG, "updatePreview error, return");
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroudHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startBackgourdThread() {
        backgroudThread = new HandlerThread("camera backgroud");
        backgroudThread.start();
        backgroudHandler = new Handler(backgroudThread.getLooper());
    }

    private void stopBackgroudThread() {
        backgroudThread.quitSafely();
        try {
            backgroudThread.join();
            backgroudThread = null;
            backgroudHandler = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** INNER CLASS */

    private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private CameraDevice.StateCallback cameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
        }
    };

    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            createCameraPreview();
        }
    };
}