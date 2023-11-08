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

    //---------------------------------------------------------------------------

    //Initialise claw state
    boolean ClawOpen = false;
    boolean WristOut = false;

    //---------------------------------------------------------------------------

    //Set Speed
    static final double ClimbSpeedUp = -0.5;
    static final double ClimbSpeedDown = 1;
    static final double LiftSpeed = -0.5;

    //---------------------------------------------------------------------------

    //Set Endpoints
    int maxLiftEncoderCount = -5000;
    int minLiftEncoderCount = 0;
    int maxClimbEncoderCount = 5000;
    int minClimbEncoderCount = 0;

    //---------------------------------------------------------------------------

    //Set Set points
    int LiftSetPtIntake = -100;
    int LiftSetPtLvl1 = -2000;
    int LiftSetPtLvl2 = -4000;
    int ClimbSetPtOut = 2500;
    int ClimbSetPtUp = 1000;

    //---------------------------------------------------------------------------
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

//---------------------------------------------------------------------------

        //Encoder Mode
        Lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Climb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Climb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Enable Break
        Climb.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        Lift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

//---------------------------------------------------------------------------

        //Servo Declaration
        Servo Wrist = hardwareMap.servo.get("Wrist");
        Servo Claw = hardwareMap.servo.get("Claw");

        //Initialise Servos
        Claw.setPosition(0.2);
        Wrist.setPosition(0.05);

        //Track the previous state of buttons
        boolean previousRBumperButtonState = false;
            //Claw Control
        boolean previousLBumperButtonState = false;
            //Wrist Control

//---------------------------------------------------------------------------

        //Verify Robot Waiting
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();

//---------------------------------------------------------------------------

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            //Initialise Encoders
            int currentLiftPosition = Lift.getCurrentPosition();
            int currentClimbPosition = Climb.getCurrentPosition();

//---------------------------------------------------------------------------

            //Mecanum Driving with Triggers
            if (gamepad1.left_trigger>0.1){
                //Strafe Left
                frontLeftMotor.setPower(-gamepad1.left_trigger);
                frontRightMotor.setPower(gamepad1.left_trigger);
                backLeftMotor.setPower(gamepad1.left_trigger);
                backRightMotor.setPower(-gamepad1.left_trigger);
            }
            else if (gamepad1.right_trigger>0.1){
                //Strafe Right
                frontLeftMotor.setPower(gamepad1.right_trigger);
                frontRightMotor.setPower(-gamepad1.right_trigger);
                backLeftMotor.setPower(-gamepad1.right_trigger);
                backRightMotor.setPower(gamepad1.right_trigger);
            }
            else{
                //Normal POV Drive
                double drive = -gamepad1.left_stick_y;
                double turn = gamepad1.left_stick_x;
                frontLeftMotor.setPower(Range.clip(drive+turn,-1.0,1.0));
                backLeftMotor.setPower(Range.clip(drive+turn,-1.0,1.0));
                frontRightMotor.setPower(Range.clip(drive-turn,-1.0,1.0));
                backRightMotor.setPower(Range.clip(drive-turn,-1.0,1.0));
            }

//---------------------------------------------------------------------------

            //Control Lift
            double lifting = gamepad1.right_stick_y;
            Lift.setPower(lifting*LiftSpeed);

//---------------------------------------------------------------------------

            //Climb Control
            if (gamepad1.dpad_up)
                Climb.setPower(ClimbSpeedUp);
            else if (gamepad1.dpad_down)
                Climb.setPower(ClimbSpeedDown);
            else
                Climb.setPower(0.0);

//---------------------------------------------------------------------------

            //Button State Check
            boolean currentRBumperButtonState = gamepad1.right_bumper;
                //Claw Control
            boolean currentLBumperButtonState = gamepad1.left_bumper;
                //Wrist Control

//---------------------------------------------------------------------------

            //Toggle Claw
            // Check if the button is currently pressed and was not pressed in the previous iteration
            if (currentLBumperButtonState && !previousLBumperButtonState) {
                if (WristOut) {
                    Wrist.setPosition(0.05);
                    //Wrist in
                } else {
                    Wrist.setPosition(0.3);
                    //Wrist out
                }
                WristOut = !WristOut; //Toggle the flag
            }

//---------------------------------------------------------------------------

            //Toggle Wrist
            // Check if the button is currently pressed and was not pressed in the previous iteration
            if (currentRBumperButtonState && !previousRBumperButtonState) {
                if (ClawOpen) {
                    Claw.setPosition(0);
                    //Claw close
                } else {
                    Claw.setPosition(0.2);
                    //Claw open
                }
                ClawOpen = !ClawOpen; //Toggle the flag
            }

//---------------------------------------------------------------------------

            //Update previous button states
            previousRBumperButtonState = currentRBumperButtonState;
                //Claw Control
            previousLBumperButtonState = currentLBumperButtonState;
                //Wrist Control

//---------------------------------------------------------------------------

            //Telemetry Update
                //Drive Information
            telemetry.addData("Left Stick X", gamepad1.left_stick_x);
            telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
            telemetry.addData("Strafe Left", gamepad1.left_trigger);
            telemetry.addData("Strafe Right", gamepad1.right_trigger);
                //Lift Information
            telemetry.addData("Lift Power", gamepad1.right_stick_y);
//            telemetry.addData("Lift Position", currentLiftPosition);
                //Climb Information
            telemetry.addData("Climb State", gamepad1.dpad_up ? "Up" : "Down");
//            telemetry.addData("Climb Position", currentClimbPosition);
                //Claw Information
            telemetry.addData("Claw State", ClawOpen ? "Open" : "Closed");
                //Wrist Information
            telemetry.addData("Wrist State", WristOut ? "Out" : "In");
                //Update
            telemetry.update();
        }
    }
}
