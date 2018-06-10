package morelifeinc.gtuc;

/**
 * Created by Mr. Stephen Asare on 6/9/2018.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PdfAdapter extends ArrayAdapter<Pdf>
{
    Activity activity;
    int layoutResourceId;
    ArrayList<Pdf> data=new ArrayList<Pdf>();
    Pdf pdf;

    public PdfAdapter(Activity activity, int layoutResourceId, ArrayList<Pdf> data) {
        super(activity, layoutResourceId, data);
        this.activity=activity;
        this.layoutResourceId=layoutResourceId;
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        PdfHolder holder=null;
        if(row==null)
        {
            LayoutInflater inflater=LayoutInflater.from(activity);
            row=inflater.inflate(layoutResourceId,parent,false);
            holder=new PdfHolder();
            holder.number1= (TextView) row.findViewById(R.id.number1);
            holder.appliance= (TextView) row.findViewById(R.id.appliance);
            holder.watts= (TextView) row.findViewById(R.id.watts);
            holder.acc33= (TextView) row.findViewById(R.id.acc33);

            row.setTag(holder);
        }
        else
        {
            holder= (PdfHolder) row.getTag();
        }

        pdf = data.get(position);

        holder.number1.setText(pdf.getDepartment());
        holder.appliance.setText(pdf.getProgram());
        holder.acc33.setText(pdf.getAcademicyear());
        holder.watts.setText(pdf.getLname());

        return row;
    }


    class PdfHolder
    {
        TextView number1,appliance,watts,acc33;
    }

}