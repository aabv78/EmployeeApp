package aabv78.com.employeeapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import aabv78.com.employeeapp.R;


public class EmployeeViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public ImageView deleteEmployee;
    public  ImageView editEmployee;

    public EmployeeViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.employee_name);
        deleteEmployee = (ImageView)itemView.findViewById(R.id.delete_employee);
        editEmployee = (ImageView)itemView.findViewById(R.id.edit_employee);
    }
}
