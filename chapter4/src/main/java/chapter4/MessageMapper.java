package chapter4;

import java.io.Serializable;

import org.apache.storm.trident.tuple.TridentTuple;

public interface MessageMapper extends Serializable {
    public String toMessageBody(TridentTuple tuple);
}