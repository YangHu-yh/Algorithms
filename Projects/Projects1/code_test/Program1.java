/*
 * Name: Yang Hu
 * EID: YH8473
 */

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 *
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 *
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Determines whether a candidate Matching represents a solution to the
     * Stable Marriage problem. Study the description of a Matching in the
     * project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
        /* TODO implement this function */
        int size = marriage.getNumberOfAdvisers();
        int npairs = marriage.getStudentMatching().size();
        if(npairs < size) return false;
        // import advisors' preferences
        ArrayList<ArrayList<Integer>> advisor_pref =  buildAdvisorPreference(marriage);
        // for each student
        for(int studenti = 0; studenti < size; studenti++){
            // go through advisor m0 preference list from top to m0's current partner
            int advisori = marriage.getStudentMatching().get(studenti);
            for(int studentj = 0; studentj < size; studentj++){
                // if the paired studentj prefer another advisorj who prefer studenti to studentj, return false.
                int advisorj = marriage.getStudentMatching().get(studentj);
                // find rank of advisors
                int rank_ai = marriage.getStudentPreference().get(studenti).indexOf(advisori);
                int rank_aj = marriage.getStudentPreference().get(studenti).indexOf(advisorj);
                // find the GPA for studenti and studentj
                Double GPAi = marriage.getStudentGPAs().get(studenti);
                Double GPAj = marriage.getStudentGPAs().get(studentj);
                // If studenti GPA is better, return false
                if(studenti != studentj && rank_ai > rank_aj && GPAi > GPAj ){
                    return false;
                }else if(studenti != studentj && rank_ai > rank_aj && GPAi.equals(GPAj)){ // else if same GPA and studenti is closer
                    // find all location coordinates and calculate distance between students to advisors
                    Double studenti_x = marriage.getStudentLocations().get(studenti).x;
                    Double studenti_y = marriage.getStudentLocations().get(studenti).y;
                    Double studentj_x = marriage.getStudentLocations().get(studentj).x;
                    Double studentj_y = marriage.getStudentLocations().get(studentj).y;
                    Double adv_x = marriage.getAdviserLocations().get(advisorj).x;
                    Double adv_y = marriage.getAdviserLocations().get(advisorj).y;
                    Double disti = (studenti_x - adv_x)*(studenti_x - adv_x) + (studenti_y - adv_y)*(studenti_y - adv_y);
                    Double distj = (studentj_x - adv_x)*(studentj_x - adv_x) + (studentj_y - adv_y)*(studentj_y - adv_y);
                    if(disti < distj){
                        return false;
                    }
                }
            }
        }
        // stable(true) for all other cases
        return true;
    }


    /**
     * Determines a solution to the Stable Marriage problem from the given input
     * set. Study the project description to understand the variables which
     * represent the input to your solution.
     *
     * @return A stable Matching.
     */
    public Matching stableMarriageGaleShapley(Matching marriage) {
        /* TODO implement this function */
        // Create list of all advisors and students and their preference list and let it represent advisors and students who are free
        ArrayList<Integer> advisors = new ArrayList<>();
        // Create a mapping student_matching set ready for inserting pairs
        ArrayList<Integer> student_matching = new ArrayList<Integer>();;
        for(int i = 0; i < marriage.getNumberOfAdvisers(); i++){
            advisors.add(i);
            student_matching.add(-1);
        }
        // Also build the preference list for each advisors
        ArrayList<ArrayList<Integer>> advisor_pref = buildAdvisorPreference(marriage);
        // While there exist advisor who do not have any students who accept their offer or who has not tried to offer admission to all students do:
        //int pointer = 0;
        while(!advisors.isEmpty()){
            // Choose an advisori and find his or her most preferred studenti who need have been offered admission
            // Since the advisor in the previous cycle will be paired with some student anyway and will be removed, so choosing the first advisor in the advisor-free list will be good.
            int advisori = advisors.get(0);
            int preference = 0;
            while(!student_matching.contains(advisori)){
                int studenti = advisor_pref.get(advisori).get(preference);
                // If the studenti not yet have any offer
                if(student_matching.get(studenti) == -1){
                    // Advisori offers admission and pair up advisori and studenti put in to the student_matching
                    student_matching.set(studenti, advisori);
                    advisors.remove(0);
                }else{
                    // Else (this studenti has and accept another offer from advisorj, get advisor from matching set)
                    int advisorj = student_matching.get(studenti);
                    // find the rank of advisori and advisorj in studenti preference list
                    int rank_advisori = marriage.getStudentPreference().get(studenti).indexOf(advisori);
                    int rank_advisorj = marriage.getStudentPreference().get(studenti).indexOf(advisorj);
                    if(rank_advisori < rank_advisorj){
                        // if(studenti prefers advisori)
                        // pair studenti with advisori
                        student_matching.set(studenti, advisori);
                        // advisorj becomes unpaired so put advisorj back to free advisor list and remove current advisori at index 0
                        advisors.remove(0);
                        advisors.add(advisorj);
                    }
                    // else: studenti prefer current paired advisorj
                    // 					Advisor m0 remains unpaired
                }
                // if studenti does not pair up with advisori, go to the next student according to the advisor preference list
                preference++;
            }
        }
        // 	Return marriage with mapping set M
        return new Matching(marriage, student_matching);

    }



    // Build the advisors' preference list in this function
    public ArrayList<ArrayList<Integer>> buildAdvisorPreference(Matching marriage){
        // get a size of number of students/advisors, since they are equal
        int n = marriage.getNumberOfAdvisers();
        // create a nested arraylist to store preference of advisors
        ArrayList<ArrayList<Integer>> advisor_pref = new ArrayList<ArrayList<Integer>>(0);
        // Create GPA list and a ranking list together, the ranking list corresponds the GPA list, i.e. with the same index, what's in the ranking list represent who has that GPA. If there is no ties, then the corresponding ranking list has only one student. If there is ties, multiple student would be in the same ArrayList. Then we will personalize the ranking for each advisor later according to the locations.
        ArrayList<Double> GPAs = new ArrayList<>(n);
        ArrayList<LinkedList<Integer>> GPAsRank = new ArrayList<>(n);
        int gpa_n = 0; // track how many different GPA has been added
        int afterLastOccur = 0; // for later use of finding next same GPA
        // a copy of student GPAs to help find the maximum each time
        ArrayList<Double> sGPAs = new ArrayList<>(marriage.getStudentGPAs());
        // go through each student's GPA, i represents current student
        for (int i = 0; i < marriage.getNumberOfStudents(); i++) {
            // find max each time in the rest of sGPAs list, later delete the max from this list so we can find the next one
            Double max = 0.0;
            int max_index = -1;
            for(int j = 0; j < sGPAs.size(); j++){
                Double gpa_j = sGPAs.get(j);
                if(max < gpa_j){
                    max = gpa_j;
                }
            }
            // when the same GPA appears, only add this student index to ranking list, not GPAs list
            if(gpa_n >= 1 && max.equals(GPAs.get(gpa_n - 1))) {
                // find where the max is and them add to GPAsRank list
                for(afterLastOccur += 1; afterLastOccur < marriage.getStudentGPAs().size(); afterLastOccur++){
                    if(marriage.getStudentGPAs().get(afterLastOccur).equals(max)){
                        break;
                    }
                }
                GPAsRank.get(gpa_n - 1 ).add(afterLastOccur);
            }else{ // otherwise this GPA is a brand new one, add to both list
                GPAs.add(max);
                GPAsRank.add(new LinkedList<>());
                GPAsRank.get(gpa_n).add(marriage.getStudentGPAs().indexOf(max));
                afterLastOccur = marriage.getStudentGPAs().indexOf(max);
                gpa_n++;
            }
            // remove the maximum to allow discovering the second/next max
            sGPAs.remove(max);
        }

        // for each advisor
        for (int i = 0; i < marriage.getNumberOfAdvisers(); i++) {
            // each GPA get corresponding students' locations
            for(gpa_n = 0; gpa_n < GPAs.size();gpa_n++){
                // only go through sublist what has more than one student with same GPA
                if(GPAsRank.get(gpa_n).size() < 2){
                    continue;
                }
                // create new list storing square of distance for later comparison
                ArrayList<Double> sqdist = new ArrayList<>();
                // find all their distance to advisor in loop
                    // the order in ties are fixed. go from index 0 to its size
                    // an arratlist of distance created and being calculated
                    // find min distance and replace student id in GPAs from 0 to size in order
                // set all distance with in this one GPA
                for(int n_in = 0; n_in < GPAsRank.get(gpa_n).size(); n_in++){
                    int studenti = GPAsRank.get(gpa_n).get(n_in);
                    Double studenti_x = marriage.getStudentLocations().get(studenti).x;
                    Double studenti_y = marriage.getStudentLocations().get(studenti).y;
                    Double adv_x = marriage.getAdviserLocations().get(i).x;
                    Double adv_y = marriage.getAdviserLocations().get(i).y;
                    sqdist.add((studenti_x - adv_x)*(studenti_x - adv_x) + (studenti_y - adv_y)*(studenti_y - adv_y));
                }
                // In this same GPA, make the ranking personalize to advisorj
                // find max for k times, k is how many same GPAs appeared
                // a copy of sqdist to remove current min and find next min.
                ArrayList<Double> dist = new ArrayList<>(sqdist);
                int distsize = sqdist.size();
                LinkedList<Integer> ascendingGPA = new LinkedList<>();
                for(int k = 0; k < distsize; k++){
                    // find the min distance min_d in the rest of dist list
                    Double min_d = dist.get(0);
                    for(int dist_i = 0; dist_i < dist.size(); dist_i++){
                        Double distAti = dist.get(dist_i);
                        if(min_d > distAti){
                            min_d = distAti;
                        }
                    }
                    // firstly add smallest distance student to list
                    ascendingGPA.add(GPAsRank.get(gpa_n).get(sqdist.indexOf(min_d)));
                    dist.remove(min_d);
                }
                GPAsRank.set(gpa_n, ascendingGPA);
            }
            // we now have a personalized ranking for advisorj, put student index into advisor_pref list in order
            ArrayList<Integer> advisor_p = new ArrayList<Integer>(0);
            for(int GPAs_i = 0; GPAs_i < GPAsRank.size(); GPAs_i++){
                for(int GPAs_j = 0; GPAs_j < GPAsRank.get(GPAs_i).size(); GPAs_j++){
                    advisor_p.add(GPAsRank.get(GPAs_i).get(GPAs_j));
                }
            }
            advisor_pref.add(advisor_p);
        }
        return advisor_pref;
    }
}
