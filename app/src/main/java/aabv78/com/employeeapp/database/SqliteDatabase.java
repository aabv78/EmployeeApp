package aabv78.com.employeeapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import aabv78.com.employeeapp.model.Employee;


public class SqliteDatabase extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	5;
    private	static final String DATABASE_NAME = "employee";
    private	static final String TABLE_EMPLOYEE = "employee";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DEPARTMENT = "department";
    private static final String COLUMN_SALARY = "salary";

    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE	TABLE " + TABLE_EMPLOYEE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DEPARTMENT + " TEXT,"
                + COLUMN_SALARY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        onCreate(db);
    }

    public List<Employee> listEmployees(){
        String sql = "select * from " + TABLE_EMPLOYEE;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Employee> storeEmployees = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String department  = cursor.getString(2);
                int salary = Integer.parseInt(cursor.getString(3));
                storeEmployees.add(new Employee(id, name, department, salary));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeEmployees;
    }

    public void addEmployee(Employee employee){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, employee.getName());
        values.put(COLUMN_DEPARTMENT, employee.getDepartment());
        values.put(COLUMN_SALARY, employee.getSalary());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_EMPLOYEE, null, values);
    }

    public void updateEmployee(Employee employee){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, employee.getName());
        values.put(COLUMN_DEPARTMENT, employee.getName());
        values.put(COLUMN_SALARY, employee.getSalary());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_EMPLOYEE, values, COLUMN_ID	+ "	= ?",
                new String[] { String.valueOf(employee.getId())});
    }

    public Employee findEmployee(String EmployeeName){
        String query = "Select * FROM "	+ TABLE_EMPLOYEE + " WHERE " + COLUMN_NAME + " = " + EmployeeName;
        SQLiteDatabase db = this.getWritableDatabase();
        Employee mEmployee = null;
        Cursor cursor = db.rawQuery(query,	null);
        if	(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String name = cursor.getString(1);
            String department = cursor.getString(2);
            int salary = Integer.parseInt(cursor.getString(3));
            mEmployee = new Employee(id, name, department, salary);
        }
        cursor.close();
        return mEmployee;
    }

    public void deleteEmployee(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEE, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
}
