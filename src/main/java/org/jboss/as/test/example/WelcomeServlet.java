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

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple Servlet which welcomes the user by name specified
 * by the request parameter "name".
 * @author <a href="mailto:alr@jboss.org">Andrew Lee Rubinger</a>
 */
@WebServlet(urlPatterns =
{"/welcome"})
public class WelcomeServlet extends HttpServlet
{

   /**
    * serialVersionUID
    */
   private static final long serialVersionUID = 1L;

   /**
    * Name of the request parameter to be sent along
    */
   private static final String REQ_PARAM_NAME = "name";

   /**
    * Content-type to be used
    */
   private static final String CONTENT_TYPE = "text/plain";

   /**
    * Error message if no request parameter "name" is sent
    */
   private static final String ERR_MSG_NO_NAME_PARAM = "Request parameter \"" + REQ_PARAM_NAME + "\" is required";

   @EJB
   private WelcomeBean bean;

   /**
    * {@inheritDoc}
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
         IOException
   {
      // Get the name from the request
      final String name = request.getParameter(REQ_PARAM_NAME);

      // Precondition checks
      if (name == null)
      {
         response.sendError(HttpServletResponse.SC_BAD_REQUEST, ERR_MSG_NO_NAME_PARAM);
      }

      // Set content type
      response.setContentType(CONTENT_TYPE);

      // Get the welcome message
      final String welcomeMessage = bean.welcome(name);

      // Write out
      response.getWriter().write(welcomeMessage);
   }

}
