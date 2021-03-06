package obj;

import obj.util.JsfUtil;
import obj.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("convocatoriaPostulanteController")
@SessionScoped
public class ConvocatoriaPostulanteController implements Serializable {

    @EJB
    private obj.ConvocatoriaPostulanteFacade ejbFacade;
    private List<ConvocatoriaPostulante> items = null;
    private ConvocatoriaPostulante selected;

    public ConvocatoriaPostulanteController() {
    }

    public ConvocatoriaPostulante getSelected() {
        return selected;
    }

    public void setSelected(ConvocatoriaPostulante selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ConvocatoriaPostulanteFacade getFacade() {
        return ejbFacade;
    }

    public ConvocatoriaPostulante prepareCreate() {
        selected = new ConvocatoriaPostulante();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ConvocatoriaPostulanteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ConvocatoriaPostulanteUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ConvocatoriaPostulanteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ConvocatoriaPostulante> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public ConvocatoriaPostulante getConvocatoriaPostulante(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<ConvocatoriaPostulante> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ConvocatoriaPostulante> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ConvocatoriaPostulante.class)
    public static class ConvocatoriaPostulanteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ConvocatoriaPostulanteController controller = (ConvocatoriaPostulanteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "convocatoriaPostulanteController");
            return controller.getConvocatoriaPostulante(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ConvocatoriaPostulante) {
                ConvocatoriaPostulante o = (ConvocatoriaPostulante) object;
                return getStringKey(o.getIdConvocPost());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ConvocatoriaPostulante.class.getName()});
                return null;
            }
        }

    }

}
