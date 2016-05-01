package com.techm.telestra.assignment.network;

/**
 * Created by PP00344204 on 25/6/15.
 *
 * Copyright (c) 2015, TechMahindra Limited and/or its affiliates. All rights reserved.
 * TECHMAHINDRA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
public class NetworkException extends Exception {
    public static String NO_INTERNET = "NO_INTERNET";

    public NetworkException(String msg) {
        super(msg);
    }
}
