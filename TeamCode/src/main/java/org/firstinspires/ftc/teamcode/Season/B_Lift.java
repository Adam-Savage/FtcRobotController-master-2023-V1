package org.firstinspires.ftc.teamcode.Season;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

@TeleOp
public class B_Lift extends LinearOpMode {

    //Set Speed
    static final double LiftSpeedUp = 0.2;
    static final double LiftSpeedDown = 0.2;

    @Override
    public void runOpMode() throws InterruptedException {

        //Motor Declaration
        DcMotor Lift = hardwareMap.dcMotor.get("Lift");

        //Encoder Mode
        Lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Enable Break
        Lift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        //Verify Robot Waiting
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            //?
            //double Lift = gamepad1.right_stick_y;

            //Control Lift
            if (gamepad1.right_stick_y > 0.2)
                Lift.setPower(LiftSpeedUp);
            else if (gamepad1.right_stick_y < 0.2)
                Lift.setPower(LiftSpeedDown);
            else
                Lift.setPower(0.0);

            //Telemetry Update
            telemetry.addData("Lift Power", gamepad1.right_stick_y);
            telemetry.update();

        }
    }
}
