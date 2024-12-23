package com.li.myRPCVersion4.codec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    REQUEST(0),RESPONSE(1);
    private int code;
}
