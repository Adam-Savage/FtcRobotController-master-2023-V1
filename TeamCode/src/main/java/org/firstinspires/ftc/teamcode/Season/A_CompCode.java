package org.firstinspires.ftc.teamcode.Season;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

@TeleOp
public class A_CompCode extends LinearOpMode {

    //Initialise claw state
    boolean isOpen = false;

    //Set Speed
    static final double ClimbSpeedUp = -0.5;
    static final double ClimbSpeedDown = 1;
    static final double LiftSpeed = -0.5;

    //Set Endpoints
    int maxLiftEncoderCount = -5000;
    int minLiftEncoderCount = 0;
    int maxClimbEncoderCount = 5000;
    int minClimbEncoderCount = 0;

    //Set Set points
    int LiftSetPtIntake = -100;
    int LiftSetPtLvl1 = -2000;
    int LiftSetPtLvl2 = -4000;
    int ClimbSetPtOut = 2500;
    int ClimbSetPtUp = 1000;

    @Override
    public void runOpMode() throws InterruptedException {

        //Motor Declaration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("Leftfront");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("Leftback");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("Rightfront");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("Rightback");
        DcMotor Lift = hardwareMap.dcMotor.get("Lift");
        DcMotor Climb = hardwareMap.dcMotor.get("Climb");

        //Motor Reverse
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //Encoder Mode
        Lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Climb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Climb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Enable Break
        Climb.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        Lift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        //Servo Declaration
        Servo Wrist = hardwareMap.servo.get("Wrist");
        Servo Claw = hardwareMap.servo.get("Claw");

        //Initialise Servos
        Wrist.setPosition(0);
        Claw.setPosition(0);

        //Verify Robot Waiting
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            //Initialise Encoders
            int currentLiftPosition = Lift.getCurrentPosition();
            int currentClimbPosition = Climb.getCurrentPosition();

            //Mecanum Driving with Triggers
            if (gamepad1.left_trigger>0.1){
                frontLeftMotor.setPower(-gamepad1.left_trigger);
                frontRightMotor.setPower(gamepad1.left_trigger);
                backLeftMotor.setPower(gamepad1.left_trigger);
                backRightMotor.setPower(-gamepad1.left_trigger);
            }
            else if (gamepad1.right_trigger>0.1){
                frontLeftMotor.setPower(gamepad1.right_trigger);
                frontRightMotor.setPower(-gamepad1.right_trigger);
                backLeftMotor.setPower(-gamepad1.right_trigger);
                backRightMotor.setPower(gamepad1.right_trigger);
            }
            else{
                double drive = -gamepad1.left_stick_y;
                double turn = gamepad1.left_stick_x;
                frontLeftMotor.setPower(Range.clip(drive+turn,-1.0,1.0));
                backLeftMotor.setPower(Range.clip(drive+turn,-1.0,1.0));
                frontRightMotor.setPower(Range.clip(drive-turn,-1.0,1.0));
                backRightMotor.setPower(Range.clip(drive-turn,-1.0,1.0));
            }

            //Toggle Grab
            // Check if button A is pressed to toggle the servo
//            if (gamepad1.a) {
//                if (isOpen) {
//                    Claw.setPosition(0.50);
//                    // Close the servo
//                } else {
//                    Claw.setPosition(0.3);
//                    // Open the servo
//                }
//                isOpen = !isOpen; // Toggle the flag
//            }

            //Wrist Control
            if (gamepad2.dpad_up)
                Wrist.setPosition(-1);
            else if (gamepad2.dpad_down)
                Wrist.setPosition(0.4);
            else
                Wrist.setPosition(0);

            //Claw Control
            if(gamepad2.dpad_right)
                Claw.setPosition(0.1);
            else
                Claw.setPosition(-0.1);

//            //Climb Soft Limits
//            if (currentClimbPosition < minClimbEncoderCount) {
//                Climb.setPower(0.0);
//                Climb.setTargetPosition(minClimbEncoderCount);
//                Climb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            } else if (currentClimbPosition > maxClimbEncoderCount) {
//                Climb.setPower(0.0);
//                Climb.setTargetPosition(maxClimbEncoderCount);
//                Climb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            }

            //Climb Control
            if (gamepad1.dpad_up)
                Climb.setPower(ClimbSpeedUp);
            else if (gamepad1.dpad_down)
                Climb.setPower(ClimbSpeedDown);
            else
                Climb.setPower(0.0);

//            //Climb Set Points
//            if (gamepad1.dpad_up) {
//                Climb.setTargetPosition(ClimbSetPtOut);
//                Climb.setPower(ClimbSpeedUp);
//            } else if (gamepad1.dpad_down) {
//                Climb.setTargetPosition(ClimbSetPtUp);
//                Climb.setPower(ClimbSpeedDown);
//            }

//            //Lift Soft Limits
//            if (currentLiftPosition < minLiftEncoderCount) {
//                Lift.setPower(0.0);
//                Lift.setTargetPosition(minLiftEncoderCount);
//                Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            } else if (currentLiftPosition > maxLiftEncoderCount) {
//                Lift.setPower(0.0);
//                Lift.setTargetPosition(maxLiftEncoderCount);
//                Lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            }

            //Control Lift
            double lifting = gamepad1.right_stick_y;
            Lift.setPower(lifting*LiftSpeed);

//            //Lift Set Points
//            if (gamepad1.dpad_right) {
//                Lift.setTargetPosition(LiftSetPtIntake);
//                Lift.setPower(LiftSpeed);
//            } else if (gamepad1.right_bumper) {
//                Lift.setTargetPosition(LiftSetPtLvl1);
//                Lift.setPower(LiftSpeed);
//            } else if (gamepad1.left_bumper) {
//                Lift.setTargetPosition(LiftSetPtLvl2);
//                Lift.setPower(LiftSpeed);
//            }

            //Telemetry Update
            telemetry.addData("Left Stick X", gamepad1.left_stick_x);
            telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
            telemetry.addData("Strafe Left", gamepad1.left_trigger);
            telemetry.addData("Strafe Right", gamepad1.right_trigger);
            telemetry.addData("Claw State", isOpen ? "Open" : "Closed");
            telemetry.addData("Lift Power", gamepad1.right_stick_y);
            telemetry.addData("Lift Position", currentLiftPosition);
            telemetry.addData("Climb State", gamepad1.dpad_up ? "Up" : "Down");
            telemetry.addData("Climb Position", currentClimbPosition);
            telemetry.update();
        }
    }
}
