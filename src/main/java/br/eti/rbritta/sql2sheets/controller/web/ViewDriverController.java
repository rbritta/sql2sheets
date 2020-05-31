package br.eti.rbritta.sql2sheets.controller.web;

import br.eti.rbritta.sql2sheets.controller.api.driver.DriverController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/drivers")
public class ViewDriverController {

    @Autowired
    private DriverController driverApi;

    @Autowired
    private NavigationHandler nav;

    @GetMapping
    public String openDriversPage(Model model) {
        return nav.renderDriver(model, driverApi.getAll());
    }

    @PostMapping("/create")
    public String createDataSource(RedirectAttributes model, MultipartFile file) {
        try {
            driverApi.create(file);
            return redirect(model, "Driver incluído com sucesso. Reinicie o Sistema para carregar a nova dependência.");
        } catch (Exception e) {
            return redirect(model, e.getMessage());
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteDataSource(RedirectAttributes model, @PathVariable String id) {
        try {
            driverApi.delete(id);
            return redirect(model, "Driver removido com sucesso. Reinicie o Sistema para atualizar as dependências.");
        } catch (Exception e) {
            return redirect(model, e.getMessage());
        }
    }

    private String redirect(RedirectAttributes model, String message) {
        return nav.redirectDrivers(model, message, driverApi.getAll());
    }
}
