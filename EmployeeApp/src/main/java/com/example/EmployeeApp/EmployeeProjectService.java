package com.example.EmployeeApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmployeeProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeProjectService.class);

    private final List<SimpleDateFormat> dateFormats = Arrays.asList(
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("dd/MM/yyyy")
            // add more formats as needed
    );

    public List<Object[]> computeOverlap(MultipartFile file) throws Exception {
        List<String[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.split(","));
            }
        }

        Map<String, List<ProjectPeriod>> employeeProjects = new HashMap<>();


        for (String[] tokens : lines) {
            System.out.println("Row: " + Arrays.toString(tokens));

            if (tokens.length == 0) continue;

            if (tokens.length < 4) {
                System.err.println("Invalid row in file: " + Arrays.toString(tokens));
                continue;
            }
            String empID = tokens[0].trim();
            String projectID = tokens[1].trim();
            Date dateFrom = parseDate(tokens[2].trim());
            Date dateTo = "NULL".equalsIgnoreCase(tokens[3].trim()) ? new Date() : parseDate(tokens[3].trim());

            if (dateFrom != null && dateTo != null) {
                employeeProjects.computeIfAbsent(empID, k -> new ArrayList<>()).add(new ProjectPeriod(empID, projectID, dateFrom, dateTo));
            }
        }

        List<Object[]> overlaps = new ArrayList<>();
        for (String emp1 : employeeProjects.keySet()) {
            for (String emp2 : employeeProjects.keySet()) {
                if (!emp1.equals(emp2)) {
                    for (ProjectPeriod pp1 : employeeProjects.get(emp1)) {
                        for (ProjectPeriod pp2 : employeeProjects.get(emp2)) {
                            if (pp1.getProjectID().equals(pp2.getProjectID())) {
                                long overlap = calculateOverlap(pp1.getDateFrom(), pp1.getDateTo(), pp2.getDateFrom(), pp2.getDateTo());
                                if (overlap > 0) {
                                    overlaps.add(new Object[]{emp1, emp2, pp1.getProjectID(), overlap});
                                    System.out.println("Found period " + emp1 + " and " + emp2 + " by project " + pp1.getProjectID() + " for " + overlap + " day");
                                }

                            }
                        }
                    }
                }
            }
        }

        return overlaps;
    }

    private long calculateOverlap(Date start1, Date end1, Date start2, Date end2) {
        if (end1.before(start2) || end2.before(start1)) {
            return 0;
        }
        Date laterStart = start1.after(start2) ? start1 : start2;
        Date earlierEnd = end1.before(end2) ? end1 : end2;
        return (earlierEnd.getTime() - laterStart.getTime()) / (1000 * 60 * 60 * 24);
    }


    private Date parseDate(String dateStr) {
        for (SimpleDateFormat format : dateFormats) {
            try {
                return format.parse(dateStr);
            } catch (ParseException ignored) {}
        }
        return null;
    }
}
