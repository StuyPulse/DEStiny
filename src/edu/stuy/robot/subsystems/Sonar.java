package edu.stuy.robot.subsystems;
import static edu.stuy.robot.RobotMap.ERROR_MARGIN_SONAR;
public class Sonar extends Subsystem {
    // string is L1234R1234
    private String data;
    private double[] parser(String s){
        try{
            String noL = s.substring(1);
            String[] parts = noL.split("R");
            double left = Double.parseDouble(parts[0]);
            double right = Double.parseDouble(parts[1]);
            double[] values = new double[]{left, right};
            return values;
        }
        catch (Exception e){
            return null;
        }
    }

    private boolean isParallel(double[] d){
        return Math.abs(d[0] - d[1]) < ERROR_MARGIN_SONAR;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public Sonar(String s){
        data = s;
        // Put the freakin sonar class in here pls
    }
}
