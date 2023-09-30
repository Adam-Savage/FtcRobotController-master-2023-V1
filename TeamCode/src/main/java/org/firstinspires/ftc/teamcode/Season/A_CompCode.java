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

    boolean isOpen = false;

    //Set Speed
    static final double ClimbSpeedUp = -0.5;
    static final double ClimbSpeedDown = 1;
    static final double LiftSpeed = -0.5;
    //static final double LiftSpeedUp = -1;
    //static final double LiftSpeedDown = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {

        //Motor Declaration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("Leftfront");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("Leftback");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("Rightfront");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("Rightback");
        DcMotor Climb = hardwareMap.dcMotor.get("Climb");
        DcMotor Lift = hardwareMap.dcMotor.get("Lift");

        //Motor Reverse
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //Enable Break
        Climb.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        Lift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        //Servo Declaration
        Servo ServoL = hardwareMap.servo.get("Left");
        Servo ServoR = hardwareMap.servo.get("Right");

        //Initialise Servos
        ServoL.setPosition(0.55);
        ServoR.setPosition(0.05);

        //Verify Robot Waiting
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

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
            if (gamepad1.a && isOpen) {
                // "A" has been pressed and we are already open - so close
                ServoL.setPosition(0.50);
                ServoR.setPosition(0.10);
                isOpen = false;
            } else if (gamepad1.b && !isOpen) {
                ServoL.setPosition(0.3);
                ServoR.setPosition(0.3);
                isOpen = true;
            }

            //Control Climb
            if (gamepad1.dpad_up)
                Climb.setPower(ClimbSpeedUp);
            else if (gamepad1.dpad_down)
                Climb.setPower(ClimbSpeedDown);
            else
                Climb.setPower(0.0);

            //Control Lift
//            if (gamepad1.right_bumper)
//                Lift.setPower(LiftSpeedUp);
//            else if (gamepad1.left_bumper)
//                Lift.setPower(LiftSpeedDown);
//            else
//                Lift.setPower(0.0);
            double lifting = -gamepad1.right_stick_y;
            Lift.setPower(lifting*LiftSpeed);

            //Telemetry Update
            telemetry.addData("Left Stick X", gamepad1.left_stick_x);
            telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
            telemetry.addData("Strafe Left", gamepad1.left_trigger);
            telemetry.addData("Strafe Right", gamepad1.right_trigger);
            telemetry.addData("Claw State", isOpen ? "Open" : "Closed");
            telemetry.addData("Lift Power", gamepad1.right_stick_y);
            telemetry.addData("Climb", gamepad1.dpad_up ? "Up" : "Down");
            telemetry.update();
        }
    }
}
