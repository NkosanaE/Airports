package com.dvt_airpots.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dvt_airpots.Model.*;
import com.dvt_airpots.Model.FlightDeparture;
import com.dvt_airpots.R;

import java.util.ArrayList;
import java.util.List;

public class FlightListAdapter extends ArrayAdapter<FlightDeparture> {

    private ArrayList<FlightDeparture> dataSet;

    LayoutInflater inflater;
    public FlightListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FlightDeparture> dataSet) {
        super(context, resource, dataSet);
        this.dataSet = dataSet;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        try {

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.flight_row_item, parent, false);
                holder.txtFlightName =(TextView)convertView.findViewById(R.id.flightName);
                holder.txtDestination =(TextView)convertView.findViewById(R.id.destination);
                holder.txtFlightNumber =(TextView)convertView.findViewById(R.id.flightNumber);
                holder.txtDepartTime =(TextView)convertView.findViewById(R.id.departTime);
                holder.imgSatus =(ImageView) convertView.findViewById(R.id.imgStatus);
                holder.txtStatus =(TextView)convertView.findViewById(R.id.txtStatus);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            FlightDeparture flightDeparture = dataSet.get(position);


            holder.txtDepartTime.setText(flightDeparture.getDeparture().getScheduledTime());

            holder.txtStatus.setText(flightDeparture.getStatus());

            if (!flightDeparture.getStatus().equals("scheduled")){
                holder.imgSatus.setImageDrawable(getContext().getDrawable(R.mipmap.red_dot));
            }else{
                holder.imgSatus.setImageDrawable(getContext().getDrawable(R.mipmap.green_dot));
            }

            holder.txtFlightName.setText(flightDeparture.getAirline().getName());
            holder.txtDestination.setText(flightDeparture.getArrival().getIataCode());
            holder.txtFlightNumber.setText(flightDeparture.getFlight().getNumber());

        }catch (Exception e){

        }


        return convertView;

    }


    static class ViewHolder {
        TextView txtFlightName;
        TextView txtDestination;
        TextView txtFlightNumber;
        TextView txtDepartTime;
        ImageView imgSatus;
        TextView txtStatus;


    }
}
