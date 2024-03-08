package org.shopping.site.admin.export;

import jakarta.servlet.http.HttpServletResponse;
import org.shopping.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.util.List;

public class UserCsvExporter extends AbstractExporter{
    public void export(List<User> userList, HttpServletResponse response) throws Exception{
        super.setResponseHeader(response,"text/csv", ".csv", "users_");
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

        csvBeanWriter.writeHeader(csvHeader);

        for (User user : userList) {
            csvBeanWriter.write(user, fieldMapping);
        }

        csvBeanWriter.close();
    }
    }