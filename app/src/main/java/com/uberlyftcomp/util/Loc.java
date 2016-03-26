package com.uberlyftcomp.util;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by cc on 2015/11/6.
 */
public class Loc implements Serializable {
    private String name;
    private String address;
    private Double lat;
    private Double lon;

    public Loc(String name, String address, Double lat, Double lon) {
        this.name = name;
        this.address = address;
        this.lat =lat;
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public String getLocName() {
        return name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public LatLng getLL(){
        return new LatLng(lat,lon);
    }

    public static String parseLocListToString(ArrayList<Loc> l){
        String ret="";
        for(int i=0;i<l.size()-1;i++){
            ret = ret + l.get(i).lat + "," + l.get(i).lon + "|";
        }
        ret = ret + l.get(l.size()-1).lat + "," + l.get(l.size()-1).lon;
        return ret;
    }

    public static ArrayList<Loc> parseStringToList(String s){
        ArrayList<Loc> l =new ArrayList<>();
        while (s.indexOf('|')!=-1){
            int c = s.indexOf(',');
            Loc loc = new Loc("","",Double.parseDouble(s.substring(0,c)), Double.parseDouble(s.substring(c + 1, s.indexOf('|'))));
            l.add(loc);
            s = s.substring(s.indexOf('|')+1);
        }
        int c = s.indexOf(',');
        Loc loc = new Loc("","",Double.parseDouble(s.substring(0,c)), Double.parseDouble(s.substring(c + 1)));
        l.add(loc);
        return l;
    }

    public static String parseFullLocListToString(ArrayList<Loc> list){
        String ret="";
        for(int i=0; i<list.size();i++){
            Loc t =list.get(i);
            String s = t.getLocName()+"/"+t.getAddress()+"/"+t.getLat()+"/"+t.getLon();
            ret = ret + s + "//";
        }
        ret = ret.substring(0,ret.length()-1);
        return ret;
    }

    public static ArrayList<Loc> parseStringToFullList(String s){
        ArrayList<Loc> ret = new ArrayList<Loc>();
        String[] temp=s.split("//");
        for(int i=0; i<temp.length;i++){
            String[] t2 = temp[i].split("/");
            Loc l = new Loc(t2[0],t2[1],Double.parseDouble(t2[2]),Double.parseDouble(t2[3]));
            ret.add(l);
        }
        return ret;
    }



}
