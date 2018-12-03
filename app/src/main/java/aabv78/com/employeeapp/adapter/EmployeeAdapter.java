package aabv78.com.employeeapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import aabv78.com.employeeapp.R;
import aabv78.com.employeeapp.database.SqliteDatabase;
import aabv78.com.employeeapp.model.Employee;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeViewHolder> {


    private Context context;
    private List<Employee> listEmployees;

    private SqliteDatabase mDatabase;

    public EmployeeAdapter(Context context, List<Employee> listEmployees) {
        this.context = context;
        this.listEmployees = listEmployees;
        mDatabase = new SqliteDatabase(context);
    }
    
    
    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_layout, parent, false);
        return new EmployeeViewHolder(view);    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        final Employee singleEmployee = listEmployees.get(holder.getAdapterPosition());


        holder.name.setText(singleEmployee.getName());

        holder.editEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(singleEmployee);
            }
        });

        holder.deleteEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database

                mDatabase.deleteEmployee(singleEmployee.getId());

                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    private void editTaskDialog(final Employee employee){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_employee_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText departmentField = (EditText)subView.findViewById(R.id.enter_department);
        final EditText salaryField  = (EditText)subView.findViewById(R.id.enter_salary);


        if(employee != null){
            nameField.setText(employee.getName());
            departmentField.setText(String.valueOf(employee.getDepartment()));
            salaryField.setText(String.valueOf(employee.getSalary()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit employee");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT EMPLOYEE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String department = departmentField.getText().toString();
                final int salary = Integer.parseInt(salaryField.getText().toString());

                if(TextUtils.isEmpty(name) || salary <= 0){
                    Toast.makeText(context, "Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    mDatabase.updateEmployee(new Employee(employee.getId(), name, department, salary));
                    //refresh the activity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}
