import java.util.ArrayList;

class Coordinate {
    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public final double x;
    public final double y;
}

/**
 * A Matching represents a candidate solution to the Stable Marriage problem. A
 * Matching may or may not be stable.
 */
public class Matching {

    /** Number of advisers. */
    private Integer m;

    /** Number of students. */
    private Integer n;

    /** A list containing each student's location. */
    private ArrayList<Coordinate> student_locations;

    /** A list containing each adviser's location. */
    private ArrayList<Coordinate> adviser_locations;

    /** A list containing each resident's preference list. */
    private ArrayList<ArrayList<Integer>> student_preference;

    /** Number of slots available in each hospital. */
    private ArrayList<Double> student_GPAs;

    /**
     * Matching information representing the index of adviser a student is
     * matched to.
     * 
     * An empty matching is represented by a null value for this field.
     */
    private ArrayList<Integer> student_matching;

    public Matching(Integer m, Integer n,
                    ArrayList<Coordinate> student_locations,
                    ArrayList<Coordinate> adviser_locations,
                    ArrayList<ArrayList<Integer>> student_preference,
                    ArrayList<Double> student_GPAs) {
        this.m = m;
        this.n = n;
        this.student_locations = student_locations;
        this.adviser_locations = adviser_locations;
        this.student_preference = student_preference;
        this.student_GPAs = student_GPAs;
        this.student_matching = null;
    }

    public Matching(Integer m, Integer n,
                    ArrayList<Coordinate> student_locations,
                    ArrayList<Coordinate> adviser_locations,
                    ArrayList<ArrayList<Integer>> student_preference,
                    ArrayList<Double> student_GPAs,
                    ArrayList<Integer> student_matching) {
        this.m = m;
        this.n = n;
        this.student_locations = student_locations;
        this.adviser_locations = adviser_locations;
        this.student_preference = student_preference;
        this.student_GPAs = student_GPAs;
        this.student_matching = student_matching;
    }

    /**
     * Constructs a solution to the stable marriage problem, given the problem
     * as a Matching. Take a Matching which represents the problem data with no
     * solution, and a student_matching which solves the problem given in data.
     * 
     * @param data
     *            The given problem to solve.
     * @param resident_matching
     *            The solution to the problem.
     */
    public Matching(Matching data, ArrayList<Integer> student_matching) {
        this(data.m, data.n, data.student_locations, data.adviser_locations,
                data.student_preference, data.student_GPAs,
                student_matching);
    }

    /**
     * Creates a Matching from data which includes an empty solution.
     * 
     * @param data
     *            The Matching containing the problem to solve.
     */
    public Matching(Matching data) {
        this(data.m, data.n, data.student_locations, data.adviser_locations,
                data.student_preference, data.student_GPAs,
                new ArrayList<Integer>(0));
    }

    public Integer getNumberOfAdvisers() {
        return m;
    }

    public Integer getNumberOfStudents() {
        return n;
    }

    public ArrayList<Coordinate> getStudentLocations() {
        return student_locations;
    }

    public ArrayList<Coordinate> getAdviserLocations() {
        return adviser_locations;
    }

    public ArrayList<ArrayList<Integer>> getStudentPreference() {
        return student_preference;
    }

    public ArrayList<Double> getStudentGPAs() {
        return student_GPAs;
    }

    public ArrayList<Integer> getStudentMatching() {
        return student_matching;
    }

    public void setResidentMatching(ArrayList<Integer> resident_matching) {
        this.student_matching = resident_matching;
    }
    
    public String getInputSizeString() {
        return String.format("m=%d n=%d\n", m, n);
    }
    
    public String getSolutionString() {
        if (student_matching == null) {
            return "";
        }
        
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < student_matching.size(); i++) {
            String str = String.format("Student %d Adviser %d", i, student_matching.get(i));
            s.append(str);
            if (i != student_matching.size() - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }
    
    public String toString() {
        return getInputSizeString() + getSolutionString();
    }
}
