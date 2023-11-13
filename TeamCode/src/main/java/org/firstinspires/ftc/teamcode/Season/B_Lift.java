package org.firstinspires.ftc.teamcode.Season;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class B_Lift extends LinearOpMode {

//---------------------------------------------------------------------------

//    //Set Speed
//    static final double ManualLiftSpeed = -0.5;
//    static final double AutoLiftSpeed = -1;

//---------------------------------------------------------------------------

//    //Set Endpoints
//    int maxLiftEncoderCount = 5000;
//    int minLiftEncoderCount = 0;

//---------------------------------------------------------------------------

//    //Set Set points
//    int LiftSetPtIntake = 100;
//    int LiftSetPtLvl1 = 2000;
//    int LiftSetPtLvl2 = 4000;

//---------------------------------------------------------------------------

    private PIDController controller;

    public static double p = 0, i = 0, d = 0;
    public static double f = 0;

    public static int target = 0;

    private final double ticks_in_degree = 700 / 180.0;

    private DcMotorEx Lift;

    @Override
    public void runOpMode() throws InterruptedException {

//---------------------------------------------------------------------------

        //Motor Declaration
        DcMotor Lift = hardwareMap.dcMotor.get("Lift");

//---------------------------------------------------------------------------

//        //Encoder Mode
//        Lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        //Enable Break
//        Lift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
//
//        //Verify Robot Waiting
//        telemetry.addData(">", "Robot Ready.  Press Play.");
//        telemetry.update();

//---------------------------------------------------------------------------

            controller = new PIDController(p, i, d);
            telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

//            //Initialise Encoders
//            int currentLiftPosition = Lift.getCurrentPosition();
//
//            //Lift Soft Limits
//            if (currentLiftPosition < minLiftEncoderCount) {
//                Lift.setTargetPosition(minLiftEncoderCount+10);
//                Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                Lift.setPower(1);
//                while (opModeIsActive() && Lift.isBusy()) {
//                    currentLiftPosition = Lift.getCurrentPosition();
//                    if(Math.abs(Lift.getTargetPosition() - currentLiftPosition) > 50) {
//                        break;
//                    }
//                }
//                Lift.setPower(LiftSpeed);
//            } else if (currentLiftPosition > maxLiftEncoderCount) {
//                Lift.setPower(0.0);
//                Lift.setTargetPosition(maxLiftEncoderCount);
//                Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            }  else {
//                Lift.setPower(LiftSpeed);
//            }
//
//            //GPT Test Set Point
//            if (gamepad1.a) {
//                // 'A' button is pressed, set the target position and power
//                Lift.setTargetPosition(50);
//                Lift.setPower(1);
//
//                // Set the motor run mode to RUN_TO_POSITION
//                Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//                // Wait until the motor reaches the target position
//                while (opModeIsActive() && Lift.isBusy()) {
//                    // Optionally, you can add telemetry to display motor position
//                    telemetry.addData("Motor Position", Lift.getCurrentPosition());
//                    telemetry.update();
//                }
//
//                // Stop the motor when it reaches the target position
//                Lift.setPower(0.0);
//
//                // Set the motor run mode back to RUN_USING_ENCODER
//                Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            } else if (gamepad1.b) {
//                Lift.setTargetPosition(900);
//                Lift.setPower(1);
//
//                Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//                while (opModeIsActive() && Lift.isBusy()) {
//                    telemetry.addData("Motor Position", Lift.getCurrentPosition());
//                    telemetry.update();
//                }
//
//                Lift.setPower(0);
//                Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            } else {
//                double lifting = gamepad1.right_stick_y;
//                Lift.setPower(lifting * ManualLiftSpeed);
//            }

            controller.setPID(p, i, d);
            int armPos = Lift.getCurrentPosition();
            double pid = controller.calculate(armPos, target);
            double ff = Math.cos(Math.toRadians(target / ticks_in_degree)) * f;

            double power = pid + ff;

            Lift.setPower(power);

            telemetry.addData("pos", armPos);
            telemetry.addData("target", target);
            telemetry.update();



            //Telemetry Update
            telemetry.addData("Lift Power", gamepad1.right_stick_y);
            telemetry.addData("Lift Position", Lift.getCurrentPosition());
            telemetry.update();

            }
        }
    }
