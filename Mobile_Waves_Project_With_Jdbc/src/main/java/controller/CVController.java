package controller;

import exception.InvalidOperationException;
import exception.NonExistingEntityException;
import lombok.extern.slf4j.Slf4j;
import model.Applicant;
import model.CV;
import service.CVService;
import service.UserService;
import view.Menu;
import view.cv.NewCvDialog;
import view.cv.UpdateCVDialog;

import java.util.List;

import static controller.MainController.userLoggedIn;

@Slf4j
public class CVController {
    private CVService cvService;
    private UserService userService;

    public CVController(CVService cvService, UserService userService) {
        this.cvService = cvService;
        this.userService = userService;
    }

    public void init() {
        var menu = new Menu("CV Menu", List.of(
                new Menu.Option("Add CV", () -> {
                    var cv = new NewCvDialog().input();
                        var applicant = userLoggedIn;
                        cvService.createCV(cv, applicant.getId());
                    return String.format("");
                }),
                new Menu.Option("Edit CV", () -> {
                    Applicant applicant = null;
                    try {
                        applicant = userService.getApplicantById(userLoggedIn.getId());
                    var applicantCV = applicant.getCv();
                    if (applicantCV == null) {
                        return "You don't have cv";
                    }
                    var cv = new UpdateCVDialog().input();
                        cvService.updateCV(applicantCV.getId(), cv);
                        System.out.println(applicant.getCv());
                    } catch (NonExistingEntityException e) {
                        e.printStackTrace();
                    } catch (InvalidOperationException e) {
                        e.printStackTrace();
                    }

                    return String.format("");
                }),
                new Menu.Option("Delete CV", () -> {
                    Applicant applicant = null;
                    try {
                        applicant = userService.getApplicantById(userLoggedIn.getId());
                    if((applicant.getCv() == null)){
                        return "You don't have cv";
                    }
                    System.out.println(applicant.getCv());
                        cvService.deleteCVById(applicant.getCv().getId());
                        System.out.println(applicant.getCv());
                    } catch (NonExistingEntityException | InvalidOperationException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
        ));
        menu.show();
    }
}
