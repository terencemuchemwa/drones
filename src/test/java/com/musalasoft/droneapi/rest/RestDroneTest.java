/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.musalasoft.droneapi.rest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author user
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

@Sql(scripts = {"/data.sql"},
        config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
public class RestDroneTest {

    @Autowired
    private MockMvc mockMvc;

    public RestDroneTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of Register method, of class RestDrone.
     */
    @Test
    public void testRegister_Drone() throws Exception {
        String json = "{\"id\":121,\"serialnumber\":\"123444\",\"model\":null,\"weightlimit\":5.0,\"batterycapacity\":10.0,\"medications\":[]}";
//        String json = "{\"serialnumber\":\"123444\",\"model\":null,\"weightlimit\":5.0,\"batterycapacity\":10.0}";
        mockMvc.perform(
                post("/drones/register")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isCreated());
    }

    /**
     * Test of Register method, of class RestDrone.
     */
    @Test
    public void testLoadDrone() throws Exception {
        String json = "[{\"id\":1,\"code\":\"1\",\"name\":\"med\",\"weight\":1,\"image\":null},\n"
                + "{\"id\":2,\"code\":\"12\",\"name\":\"med1\",\"weight\":1,\"image\":null},\n"
                + "{\"id\":3,\"code\":\"13\",\"name\":\"med2\",\"weight\":1,\"image\":null}\n"
                + "]";
        mockMvc.perform(
                post("/drones/load/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isOk());
    }

    /**
     * Test of getLevel method, of class RestDrone.
     */
    @Test
    public void testGetLevel() throws Exception {
        long droneid = 1;
        mockMvc.perform(
                get("/drones/" + droneid + "/batterylevel")
        ).andExpect(status().isOk());
    }

    /**
     * Test of getMedications method, of class RestDrone.
     */
    @Test
    public void testGetMedications() throws Exception {
        long droneId = 1;
        mockMvc.perform(
                get("/drones/" + droneId + "/medications")
        )
                .andExpect(status().isOk());
    }

    /**
     * Test of listAvailable method, of class RestDrone.
     */
    @Test
    public void testListAvailable() throws Exception {
        mockMvc.perform(get("/drones/available"))
                .andExpect(status().isOk());
    }

}
