package org.firstinspires.ftc.teamcode.teleop.demonstrationfiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.WhiteBalanceControl;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp(name = "Club Fair", group = "demonstration")
public class ClubFair extends OpMode {

    private static final String CAPTURE_FOLDER = "FIRST/ClubFairImages";
    private static final String CAMERA_NAME = "camera";

    private DcMotorEx leftFront, leftRear, rightFront, rightRear;

    private VisionPortal visionPortal;
    private ImageCaptureProcessor captureProcessor;
    private boolean aWasPressed = false;
    private ElapsedTime time;
    private Servo light;
    private boolean doAnimation = false;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        captureProcessor = new ImageCaptureProcessor();

        CameraName cameraName = hardwareMap.get(WebcamName.class, CAMERA_NAME);

            visionPortal = new VisionPortal.Builder()
                .setCamera(cameraName)
                .setCameraResolution(new Size(1280, 720))
                .addProcessor(captureProcessor)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();

        light = hardwareMap.get(Servo.class, "light");

        time = new ElapsedTime();

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
        if (time.milliseconds() > 500 && gamepad1.triangle) {
            doAnimation = false;
            takePhoto();
        } else if (time.milliseconds() > 3000 && gamepad1.square) {
            doAnimation = true;
            takePhoto();
        }

        if (doAnimation && time.milliseconds() < 2700) {
            if (time.milliseconds() < 900) {
                light.setPosition(1);
            } else if(time.milliseconds() < 1200) {
                light.setPosition(0);
            } else if(time.milliseconds() < 2100) {
                light.setPosition(1);
            } else if(time.milliseconds() < 2500) {
                light.setPosition(0);
            } else {
                light.setPosition(1);
            }
        } else if (!doAnimation && time.milliseconds() < 200) {
            light.setPosition(1);
        } else {
            light.setPosition(0);
        }

        updateDrive();

        telemetry.addLine("Triangle to take pictures, square to take pictures w/ animation");
    }

    public void loop() {

    }

    private void updateDrive() {
        double leftFPower;
        double rightFPower;
        double leftBPower;
        double rightBPower;
        double drive = gamepad1.left_stick_x * 0.8;
        double turn = -gamepad1.left_stick_y * 0.6;

        double leftPower = drive + turn;
        double rightPower = drive - turn;

        // Set power to values calculated above
        leftFront.setPower(leftPower);
        leftRear.setPower(leftPower);
        rightFront.setPower(rightPower);
        rightRear.setPower(rightPower);
    }

    private void takePhoto() {
        light.setPosition(1);
        time.reset();
        captureProcessor.requestCapture();
    }

    @Override
    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }

    /**
     * VisionProcessor that ignores incoming frames unless a capture is
     * requested. When it is, that one frame is converted to a Bitmap, saved
     * to disk as a JPEG, and kept in memory. Every call to onDrawFrame()
     * paints that same still Bitmap over the whole Live View canvas, so the
     * Driver Hub always shows the most recently captured image rather than
     * a moving feed.
     */
    private static class ImageCaptureProcessor implements VisionProcessor {
        private final AtomicBoolean captureRequested = new AtomicBoolean(false);
        private volatile String lastSavedPath = "none";
        private final Object bitmapLock = new Object();
        private Bitmap lastCapturedBitmap = null;

        private final Paint placeholderTextPaint = new Paint();

        ImageCaptureProcessor() {
            placeholderTextPaint.setColor(Color.WHITE);
        }

        void requestCapture() {
            captureRequested.set(true);
        }

        String getLastSavedPath() {
            return lastSavedPath;
        }

        @Override
        public void init(int width, int height, CameraCalibration calibration) {

        }

        @Override
        public Object processFrame(Mat frame, long captureTimeNanos) {
            if (captureRequested.compareAndSet(true, false)) {
                captureAndSave(frame);
            }
            return null;
        }

        @Override
        public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight,
                                float scaleBmpPxToCanvasPx, float scaleCanvasDensity,
                                Object userContext) {
            Bitmap toDraw;
            synchronized (bitmapLock) {
                toDraw = lastCapturedBitmap;
            }

            if (toDraw != null) {
                Rect src = new Rect(0, 0, toDraw.getWidth(), toDraw.getHeight());
                Rect dst = new Rect(0, 0, onscreenWidth, onscreenHeight);
                canvas.drawBitmap(toDraw, src, dst, null);
            } else {
                canvas.drawColor(Color.BLACK);
                placeholderTextPaint.setTextSize(onscreenHeight / 15f);
                canvas.drawText("No image captured yet",
                        onscreenWidth / 2f, onscreenHeight / 2f, placeholderTextPaint);
            }
        }

        private void captureAndSave(Mat frame) {
            Mat rgbFrame = null;
            try {
                rgbFrame = new Mat();
                Imgproc.cvtColor(frame, rgbFrame, Imgproc.COLOR_RGBA2RGB);

                Bitmap bitmap = Bitmap.createBitmap(
                        rgbFrame.cols(), rgbFrame.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(rgbFrame, bitmap);

                File folder = new File(Environment.getExternalStorageDirectory(), CAPTURE_FOLDER);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                        .format(new Date()) + ".jpg";
                File outFile = new File(folder, fileName);

                try (FileOutputStream out = new FileOutputStream(outFile)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                }

                lastSavedPath = outFile.getAbsolutePath();

                synchronized (bitmapLock) {
                    lastCapturedBitmap = bitmap;
                }
            } catch (Exception e) {
                lastSavedPath = "ERROR: " + e.getMessage();
            } finally {
                if (rgbFrame != null) {
                    rgbFrame.release();
                }
            }
        }
    }
}