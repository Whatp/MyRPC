package com.li.myRPCVersion4.client;

import com.li.myRPCVersion4.common.RPCRequest;
import com.li.myRPCVersion4.common.RPCResponse;

public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
}
