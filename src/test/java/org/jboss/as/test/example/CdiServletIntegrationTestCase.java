/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.test.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author <a href="mailto:alr@jboss.org">Andrew Lee Rubinger</a>
 */
@RunWith(Arquillian.class)
public class CdiServletIntegrationTestCase
{

   //-------------------------------------------------------------------------------------||
   // Class Members ----------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   private static final String appName = "testApp";

   private static final String NAME = "ALR";

   private static final String EXPECTED_WELCOME = "Welcome, " + NAME;

   //-------------------------------------------------------------------------------------||
   // Instance Members -------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   // Inject the EJB reference to test directly
   @EJB(mappedName = "java:global/testApp/WelcomeBean!org.jboss.as.test.example.WelcomeBean")
   private WelcomeBean ejb;

   //-------------------------------------------------------------------------------------||
   // Deployment -------------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   @Deployment
   public static WebArchive getDeployment()
   {
      final WebArchive archive = ShrinkWrap.create(WebArchive.class, appName + ".war").addClasses(WelcomeBean.class,
            WelcomeServlet.class);
      System.out.println(archive.toString(true));;
      return archive;
   }

   //-------------------------------------------------------------------------------------||
   // Tests ------------------------------------------------------------------------------||
   //-------------------------------------------------------------------------------------||

   /**
    * Ensures that the business logic for welcoming via the Servlet is working as expected
    * @throws Exception
    */
   @Test
   public void ensureWelcomeMessageViaServlet() throws Exception
   {
      // Execute a simple request
      final URL url = new URL("http://localhost:8080/" + appName + "/welcome?name=" + NAME);
      final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      final String response = reader.readLine();

      // Examine the response status
      System.out.println("Got welcome message from Servlet: " + response);
      Assert.assertEquals("Welcome message from EJB was not as expected", EXPECTED_WELCOME, response);
   }

   /**
    * Ensures that the business logic for welcoming via the EJB is working as expected
    * @throws Exception
    */
   @Test
   public void ensureWelcomeMessageViaEjb() throws Exception
   {
      final String welcomeMessage = ejb.welcome(NAME);
      System.out.println("Got welcome message from EJB: " + welcomeMessage);
      Assert.assertEquals("Welcome message from EJB was not as expected", EXPECTED_WELCOME, welcomeMessage);
   }

}
