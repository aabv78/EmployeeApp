package aabv78.com.employeeapp;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

import aabv78.com.employeeapp.adapter.EmployeeAdapter;
import aabv78.com.employeeapp.database.SqliteDatabase;
import aabv78.com.employeeapp.model.Employee;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SqliteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);

        RecyclerView employeeView = (RecyclerView)findViewById(R.id.employee_list);
        employeeView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        employeeView.setLayoutManager(linearLayoutManager);


        mDatabase = new SqliteDatabase(this);
        List<Employee> allEmployees = mDatabase.listEmployees();

        if(allEmployees.size() > 0){
            employeeView.setVisibility(View.VISIBLE);
            EmployeeAdapter mAdapter = new EmployeeAdapter(this, allEmployees);
            employeeView.setAdapter(mAdapter);

        }else {
            employeeView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no employee in the database. Start adding now", Toast.LENGTH_LONG).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add new quick task
                addTaskDialog();
            }
        });
    }


    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_employee_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText departmentField = (EditText)subView.findViewById(R.id.enter_department);
        final EditText salaryField = (EditText)subView.findViewById(R.id.enter_salary);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new employee");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD EMPLOYEE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String department = departmentField.getText().toString();
                final int salary = Integer.parseInt(salaryField.getText().toString());

                if(TextUtils.isEmpty(name) || salary <= 0 || TextUtils.isEmpty(department) ){
                    Toast.makeText(MainActivity.this, "Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    Employee newEmployee = new Employee(name, department, salary);
                    mDatabase.addEmployee(newEmployee);

                    //refresh the activity
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }
    
}
