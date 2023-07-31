package com.cagecfi.workflow.views.login;

import com.cagecfi.workflow.home.WorkflowHomePage;
import com.cagecfi.workflow.model.Departement;
import com.cagecfi.workflow.model.Token;
import com.cagecfi.workflow.model.User;
import com.cagecfi.workflow.service.WorkflowHttpsSession;
import com.cagecfi.workflow.service.impl.AppUserHttpClient;
import com.cagecfi.workflow.service.impl.DepartementHttpClient;
import com.cagecfi.workflow.service.impl.UserService;
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.spring.injection.annot.SpringBean;

//@WicketHomePage
@WicketSignInPage
@MountPath("/login")
public class LoginPage extends WebPage {

    @SpringBean
    private AppUserHttpClient appUserHttpClient;

    @SpringBean
    private DepartementHttpClient departementHttpClient;

    @SpringBean
    private UserService userService;

    public LoginPage() {
        if (((SecureWebSession) getSession()).isSignedIn()){
            continueToOriginalDestination();
        }
        add(new LoginForm("loginForm"));
    }

    private class LoginForm extends Form<LoginForm>{

        private String username;
        private String password;

        public LoginForm(String id) {
            super(id);
            setModel(new CompoundPropertyModel<>(this));
            add(new FeedbackPanel("feedback").add(new AttributeModifier("class", "text-danger")));
            add(new RequiredTextField<>("username"));
            add(new PasswordTextField("password"));
        }

        @Override
        protected void onSubmit() {
            SecureWebSession session = (SecureWebSession) SecureWebSession.get();
            Token token = null;
            try {
                token = appUserHttpClient.login(username, password);
            }catch (IOException ex){
                Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE,null, ex);
            }

            if (token != null){
                userService.setToken(token.getAccessToken());
            } else {
                error("Connexion Echouée. Token non trouvé !");
                return;
            }

            if (session.signIn(username, password)){
                User user = null;
                Departement dep = null;
                try {
                    user = appUserHttpClient.profile(token.getAccessToken());
                    

                } catch (IOException ex) {
                    Logger.getLogger(LoginPage.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (user != null) {
                    session.setAttribute(WorkflowHttpsSession.SESSION_USER_ID, user.getId());
                    session.setAttribute(WorkflowHttpsSession.SESSION_USER_LOGIN, user.getUsername());
                    session.setAttribute(WorkflowHttpsSession.SESSION_USER_EMAIL, user.getEmail());
                    session.setAttribute(WorkflowHttpsSession.SESSION_USER_FULL_NAME, user.getName());
                    session.setAttribute(WorkflowHttpsSession.SESSION_ACCESS_TOKEN, token.getAccessToken());
                    session.setAttribute(WorkflowHttpsSession.SESSION_REFRESH_TOKEN, token.getRefreshToken());
                    session.setAttribute(WorkflowHttpsSession.SESSION_LIST_ROLES, (Serializable) user.getRolesString());
                    session.setAttribute(WorkflowHttpsSession.SESSION_ACCESS_TOKEN, token.getAccessToken());
                    session.setAttribute(WorkflowHttpsSession.SESSION_REFRESH_TOKEN, token.getRefreshToken());
                    
                    dep = departementHttpClient.getOneDepartmentByUser(user.getId());
                    
                    if (dep != null) {
                        session.setAttribute(WorkflowHttpsSession.SESSION_USER_DEPARTMENT_ID, dep.getId());
                        session.setAttribute(WorkflowHttpsSession.SESSION_USER_DEPARTMENT, dep.getLibelle());
                    }
                    
//                        session.setAttribute(WicketHttpsSession.SESSION_USER_FULL_NAME, user.getNomComplet());
//                        session.setAttribute(WicketHttpsSession.SESSION_USER_PROFIL, user.getRole().libelle());
//                        session.setAttribute(WicketHttpsSession.SESSION_USER_PROFIL_ID, user.getRole());
                    success("Connexion réussie.");
                    if (user.getActif() ) {
                        setResponsePage(WorkflowHomePage.class);
                    }
                } else {
                    error("Connexion Echouée. Utilisateur non trouvé !");
                }
            } else {
                error("Connexion Echouée. Session invalide !");
            }

            return;
        }

    }
}
