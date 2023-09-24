package org.firstinspires.ftc.teamcode.Season;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class D_Wrist extends LinearOpMode {

    boolean isIn = true;

    @Override
    public void runOpMode() throws InterruptedException {

        //Servo Declaration
        Servo Wrist = hardwareMap.servo.get("Wrist");

        //Initialise Servo
        Wrist.setPosition(0.0);

        //Verify Robot Waiting
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            //Toggle Position
            if (gamepad1.dpad_left && !isIn) {
                Wrist.setPosition(1.0);
                isIn = false;
            }
            if (gamepad1.dpad_left && isIn) {
                Wrist.setPosition(0.0);
                isIn = true;
            }

            //Telemetry Update
            telemetry.addData("Wrist State", isIn ? "In" : "Out");
            telemetry.update();
        }
    }
}
