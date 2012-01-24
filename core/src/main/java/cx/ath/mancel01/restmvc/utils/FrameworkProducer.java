/*
 *  Copyright 2012 Mathieu ANCELIN.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package cx.ath.mancel01.restmvc.utils;

import cx.ath.mancel01.restmvc.FrameworkFilter;
import cx.ath.mancel01.restmvc.Session;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CDI beans producer
 * 
 * @author Mathieu ANCELIN
 */
@ApplicationScoped
public class FrameworkProducer {
    
    @Produces @RequestScoped
    public Session getSession() {
        return Session.get();
    }
    @Produces @RequestScoped
    public HttpServletRequest getRequest() {
        return FrameworkFilter.getRequest();
    }
    @Produces @RequestScoped
    public HttpServletResponse getResponse() {
        return FrameworkFilter.getResponse();
    }
}
