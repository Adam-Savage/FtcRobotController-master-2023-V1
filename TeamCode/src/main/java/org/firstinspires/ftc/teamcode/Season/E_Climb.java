package org.firstinspires.ftc.teamcode.Season;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;

@TeleOp
public class E_Climb extends LinearOpMode {

    //Set Speed
    static final double ClimbSpeedUp = 0.2;
    static final double ClimbSpeedDown = 0.2;

    @Override
    public void runOpMode() throws InterruptedException {

        //Motor Declaration
        DcMotor Climb = hardwareMap.dcMotor.get("Climb");

        //Enable Break
        Climb.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        //Verify Robot Waiting
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            //Control Lift
            if (gamepad1.dpad_up)
                Climb.setPower(ClimbSpeedUp);
            else if (gamepad1.dpad_down)
                Climb.setPower(ClimbSpeedDown);
            else
                Climb.setPower(0.0);
        }
    }
}
