package com.li.myRPCVersion5.client;

import com.li.myRPCVersion5.common.RPCRequest;
import com.li.myRPCVersion5.common.RPCResponse;

public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
}
