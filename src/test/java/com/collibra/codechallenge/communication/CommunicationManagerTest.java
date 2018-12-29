package com.collibra.codechallenge.communication;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommunicationManagerTest {

    private CommunicationManager manager;

    @Before
    public void setUp() {
        manager = new CommunicationManager();
    }

    @Test
    public void getResponse_AddEdge_NegativeEdgeWeight_UnknownCommand() {
        String response = manager.getResponse("ADD EDGE A B -1");
        assertEquals("SORRY, I DIDN'T UNDERSTAND THAT", response);
    }
    @Test
    public void getResponse_AddNode_BadName_UnknownCommand() {
        String response = manager.getResponse("ADD NODE !@@##$%^%");
        assertEquals("SORRY, I DIDN'T UNDERSTAND THAT", response);
    }

    @Test
    public void getResponse_AddNode() {
        String response = manager.getResponse("ADD NODE node-A");
        assertEquals("NODE ADDED", response);
    }
}