import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Student {
    private String name;
    private int studentId;
    private List<Course> courses;

    public Student(String name, int studentId) {
        this.name = name;
        this.studentId = studentId;
        this.courses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getStudentId() {
        return studentId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void registerCourse(Course course) {
        courses.add(course);
    }

    public void dropCourse(Course course) {
        courses.remove(course);
    }
}

class Course {
    private String courseCode;
    private String courseName;
    private String description;
    private int durationInWeeks;
    private int seatCapacity;

    public Course(String courseCode, String courseName, String description, int durationInWeeks, int seatCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.durationInWeeks = durationInWeeks;
        this.seatCapacity = seatCapacity;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationInWeeks() {
        return durationInWeeks;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    private int coursesRegistered = 0;

    public int getCoursesRegistered() {
        return coursesRegistered;
    }

    public void registerStudent() {
        coursesRegistered++;
    }

    public void unregisterStudent() {
        coursesRegistered--;
    }
}


public class CourseRegistrationSystem {
    private static List<Student> students = new ArrayList<>();
    private static Map<String, Course> courseDatabase = new HashMap<>();
    private static int studentIdCounter = 1;

    public static void main(String[] args) {
        // Initialize the course database with sample courses
        courseDatabase.put("CSE101", new Course("CSE101", "Introduction to Computer Science",
                "An introduction to programming and computer science fundamentals.", 12, 2));
        courseDatabase.put("MAT202", new Course("MAT202", "Calculus II",
                "Continuation of calculus concepts, including integration and applications.", 10, 2));
        courseDatabase.put("ENG103", new Course("ENG103", "English Composition",
                "Developing effective writing skills and critical thinking in English.", 8, 4));
        // ... Add more courses ...

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register Student");
            System.out.println("2. Add Course");
            System.out.println("3. Register Course for Student");
            System.out.println("4. Drop Course for Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();
                    Student newStudent = new Student(studentName, studentIdCounter);
                    students.add(newStudent);
                    System.out.println("Student registered with ID: " + studentIdCounter);
                    studentIdCounter++;
                    break;

                case 2:
                    System.out.print("Enter course code: ");
                    String courseCode = scanner.nextLine();
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();
                    System.out.print("Enter course description: ");
                    String courseDescription = scanner.nextLine();
                    System.out.print("Enter course duration in weeks: ");
                    int durationInWeeks = scanner.nextInt();
                    System.out.print("Enter course seat capacity: ");
                    int seatCapacity = scanner.nextInt();
                    Course newCourse = new Course(courseCode, courseName, courseDescription, durationInWeeks, seatCapacity);
                    courseDatabase.put(courseCode, newCourse);
                    System.out.println("Course added successfully.");
                    break;

                case 3:
                    System.out.print("Enter student ID: ");
                    int studentId = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    Student student = findStudentById(studentId);
                    if (student == null) {
                        System.out.println("Student not found.");
                        break;
                    }

                    System.out.println("Welcome, " + student.getName() + "!");
                    System.out.println("Available courses:");
                    for (Course course : courseDatabase.values()) {
                        System.out.println(course.getCourseCode() + " - " + course.getCourseName());
                    }

                    System.out.print("Enter course code to register: ");
                    String selectedCourseCode = scanner.nextLine();
                    Course selectedCourse = findCourseByCode(selectedCourseCode);
                    if (selectedCourse == null) {
                        System.out.println("Course not found.");
                        break;
                    }


                    if (selectedCourse.getSeatCapacity() <= selectedCourse.getCoursesRegistered()) {
                        System.out.println("Course is already full. Cannot register.");
                        break;
                    }

                    if (student.getCourses().contains(selectedCourse)) {
                        System.out.println("Student is already registered for this course.");
                        break;
                    }

                    selectedCourse.registerStudent(); // Increment registered count
                    student.registerCourse(selectedCourse);
                    System.out.println("Course registered successfully.");
                    break;

                case 4:
                    System.out.print("Enter student ID: ");
                    int studentIdDrop = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline

                    Student studentDrop = findStudentById(studentIdDrop);
                    if (studentDrop == null) {
                        System.out.println("Student not found.");
                        break;
                    }

                    List<Course> studentCourses = studentDrop.getCourses();
                    if (studentCourses.isEmpty()) {
                        System.out.println("No courses registered.");
                        break;
                    }

                    System.out.println("Courses registered by " + studentDrop.getName() + ":");
                    for (int i = 0; i < studentCourses.size(); i++) {
                        System.out.println((i + 1) + ". " + studentCourses.get(i).getCourseName());
                    }

                    System.out.print("Enter the number of the course to drop: ");
                    int courseIndex = scanner.nextInt();
                    if (courseIndex < 1 || courseIndex > studentCourses.size()) {
                        System.out.println("Invalid course number.");
                        break;
                    }

                    Course courseToDrop = studentCourses.get(courseIndex - 1);
                    studentDrop.dropCourse(courseToDrop);
                    System.out.println("Course dropped successfully.");
                    break;

                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static Student findStudentById(int studentId) {
        for (Student student : students) {
            if (student.getStudentId() == studentId) {
                return student;
            }
        }
        return null;
    }

    public static Course findCourseByCode(String courseCode) {
        return courseDatabase.get(courseCode);
    }
}
