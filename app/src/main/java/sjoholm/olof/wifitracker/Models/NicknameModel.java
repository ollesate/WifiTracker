package sjoholm.olof.wifitracker.Models;

/**
 * Created by olof on 2016-01-17.
 */
public class NicknameModel {

    public String wifiName;
    public String nickName;

    @Override
    public String toString() {
        return nickName == null ? wifiName : nickName;
    }
}
