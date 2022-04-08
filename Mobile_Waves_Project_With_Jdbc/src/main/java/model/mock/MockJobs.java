package model.mock;

import model.Job;

import java.util.ArrayList;

public class MockJobs {
    public static final Job[] MOCK_JOBS;
    static {
        MOCK_JOBS = new Job[]{
                new Job("Senior .NET DEVELOPER", "One of the biggest custom software development companies in Bulgaria. Created and managed by software engineers since 2001, " +
                        "the company offers complete software solutions, based on cutting-edge technologies.", "Provide technology consulting to our clients on their system architecture and infrastructure\n" +
                        "Collaborate with the development team members to ensure scalability, resilience, security, and availability of the software products",
                        6500.00, "Experience with Linux and/or Windows operating systems\n" +
                        "Hands-on experience of installing, configuring and operating Linux and/or Windows servers", new ArrayList<>()),
                new Job("Junior Java Developer", "One of the biggest custom software development companies in Bulgaria. Created and managed by software engineers since 2001, " +
                        "the company offers complete software solutions, based on cutting-edge technologies.", "Design and develop complex state of the art software projects based on the Java technology stack\n" +
                        "Provide technical leadership for enterprise software projects",
                        6500.00, "2 years of experience with Java", new ArrayList<>()),
                new Job("Junior Sys Admin", "One of the biggest custom software development companies in Bulgaria. Created and managed by software engineers since 2001, " +
                        "the company offers complete software solutions, based on cutting-edge technologies.", "Passion for development and dealing with complex situations\n" +
                        "Software development understanding",
                        6500.00, "Development experience in JavaScript, TypeScript, etc.", new ArrayList<>()),
                new Job("Endocrinology Doctor ", "Our hospital is one of the most known hospitals.", "You'll be examining people and " +
                        "listing them some pills.",
                        3500.00, "MD in Medicine", new ArrayList<>()),
                new Job("Bartender", "Drinks done with passion", "You'll be making cocktails in entertaining way.",
                2000.00, "No previous experience required", new ArrayList<>()),
                new Job("Speedy Courier Driver", "Leading Courier Company", "All you should do is carry " +
                        "packages around the city.",
                        1500.00, "Drivers License", new ArrayList<>())

        };
    }
}
