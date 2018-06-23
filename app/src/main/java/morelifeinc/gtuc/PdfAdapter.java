package morelifeinc.gtuc;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Stephen Asare on 6/23/2018.
 */
public class PdfAdapter extends ArrayAdapter<Pdf>
{
    Activity activity;
    int layoutResourceId;
    List<Pdf> data = new ArrayList<Pdf>();
    Pdf pdf;

    public PdfAdapter(Activity activity, int layoutResourceId, ArrayList<Pdf> data) {
        super(activity, layoutResourceId, data);
        this.activity=activity;
        this.layoutResourceId=layoutResourceId;
        this.data=data;
    }

//    public Object getItem(int position) {
//        return filteredData.get(position);
//    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.depalist,parent,false);

       pdf = data.get(position);

        TextView course = (TextView) row.findViewById(R.id.number1);
        TextView ccode = (TextView) row.findViewById(R.id.appliance);
        TextView lec = (TextView) row.findViewById(R.id.watts);
        //CheckBox ch = (CheckBox) row.findViewById(R.id.checkBox);

        //pdf = data.get(position);

        course.setText(pdf.getDepartment());
        ccode.setText(pdf.getProgram());
        lec.setText(pdf.getLname());

//            course.setText(courseArray[position]);
//            ccode.setText(ccodeArray[position]);
//            lec.setText(lecArray[position]);

        return row;
    }


    public void setFilter(ArrayList<Pdf> newList)

    {
        data = new ArrayList<>();
        data.addAll(newList);
        notifyDataSetChanged();

    }

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View row=convertView;
//            PdfHolder holder=null;
//            if(row==null)
//            {
//                LayoutInflater inflater=LayoutInflater.from(activity);
//                row=inflater.inflate(layoutResourceId,parent,false);
//                holder=new PdfHolder();
//                holder.number1= (TextView) row.findViewById(R.id.number1);
//                holder.appliance= (TextView) row.findViewById(R.id.appliance);
//                holder.watts= (TextView) row.findViewById(R.id.watts);
//                //holder.acc33= (TextView) row.findViewById(R.id.acc33);
//
//                row.setTag(holder);
//            }
//            else
//            {
//                holder= (PdfHolder) row.getTag();
//            }
//
//            pdf = data.get(position);
//
//            holder.number1.setText(pdf.getDepartment());
//            holder.appliance.setText(pdf.getProgram());
//            // holder.acc33.setText(pdf.getAcademicyear());
//            holder.watts.setText(pdf.getLname());
//
//            return row;
//        }

    class PdfHolder
    {
        TextView number1,appliance,watts;
    }

    public int getCount() {
        return data.size();
    }


//removed acc3 (academic year)
}
