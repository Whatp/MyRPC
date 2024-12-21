package com.li.myRPCVersion3.client;

import com.li.myRPCVersion1.common.RPCRequest;
import com.li.myRPCVersion1.common.RPCResponse;

public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
}
