package test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
	 List<Integer> myScores = new ArrayList<>(Arrays.asList(24, 65, 92, 43, 72, 45, 58, 9, 18, 5, 52, 53, 22, 22));
     Student scoresList = new Student(myScores);
     
    }
}
