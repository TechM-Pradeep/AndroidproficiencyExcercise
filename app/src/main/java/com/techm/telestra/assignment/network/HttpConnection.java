package com.techm.telestra.assignment.network;

import java.util.Map;

/**
 * Created by PP00344204 on 25/6/15.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public interface HttpConnection<T> {
    void doGetRequest() throws NetworkException;

    void doPostRequest() throws NetworkException;

    void addPayload(Object params) throws NetworkException;

    void addFilePart(Map<String, byte[]> fileParts) throws NetworkException;

    T getResponse();

    void close();
}
