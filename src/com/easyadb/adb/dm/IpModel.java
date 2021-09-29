package com.easyadb.adb.dm;

public class IpModel implements Comparable<IpModel>{
    private String ip = "999:999:999:0";
    private int useTimes = -1;

    public IpModel() {
    }

    public IpModel(String ip) {
       this(ip,0);
    }

    public IpModel(String ip, int useTimes) {
        this.ip = ip;
        this.useTimes = useTimes;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getUseTimes() {
        return useTimes;
    }

    public void useTimesAdd(){
        useTimes++;
    }

    public void setUseTimes(int useTimes) {
        this.useTimes = useTimes;
    }

    @Override
    public int compareTo(IpModel o) {
        int i = o.getUseTimes() - getUseTimes();  //使用次数从大到小
        if(i == 0){
            return getIp().compareTo(o.getIp()); //则按照ip排序(从小到大)
        }
        return i;
    }

    @Override
    public String toString() {
        return ip + "#" + useTimes;
    }
}
