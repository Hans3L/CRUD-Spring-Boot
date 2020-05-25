package pe.creativity.Restfull.service;

import com.sun.org.apache.xerces.internal.dom.NotationImpl;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.creativity.Restfull.dto.NotaDto;
import pe.creativity.Restfull.entity.Nota;
import pe.creativity.Restfull.repository.NotaRepositorio;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class NotaServiceImpl implements NotaService {

    @Autowired
    private NotaRepositorio notaRepositorio;

    private static final Log logger = LogFactory.getLog(NotaServiceImpl.class);

    @Override
    public List<Nota> findAllNotas() {
        logger.warn("LISTANDO TODA LA DATA");
        return notaRepositorio.findAll();
    }

    @Override
    public List<Nota> getPaginacion(Pageable pageable) {
        return notaRepositorio.findAll(pageable).getContent();
    }

//    @Override
//    public Optional findNotasById(Long id) {
//        Optional optional = notaRepositorio.findById(id);
//        return optional;
//    }

    @Override
    public Nota saveNota(Nota notaNew) {
        logger.info("Creando un nuevo usuario");
        if (notaNew != null) {
            return notaRepositorio.save(notaNew);
        }
        return new Nota();
    }

    @Override
    public void saveNote(String nombre, String titulo, String contenido) throws NoSuchElementException {
        logger.info("Creando un usuario usando DTO");
        logger.info(nombre);
        logger.info(titulo);
        logger.info(contenido);
        notaRepositorio.save(new Nota(nombre, titulo, contenido));

    }

//    @Override
//    public Optional<Nota> findNotasById(Long id) {
//        logger.info("Llamando a un usuario");
//        logger.info(id);
//        //Page<Nota> notaEntity = notaRepositorio.findById(id);
//        return notaRepositorio.findById(Optional.of(id));
//    }

    @Override
    public String deleteNota(Long id) {
        try {
            notaRepositorio.deleteById(id);
            return "Identificador Borrado de Nota";
        } catch (Exception e) {
            return "Identificador No Borrado de Nota " + e;
        }
    }

    @Override
    public String updateNota(Nota notaNew) {
        Long num = notaNew.getId();
        logger.info(num);
        if (notaRepositorio.findById(num).equals(true)) {
            Nota notaToUpdate = new Nota();
            notaToUpdate.setId(notaToUpdate.getId());
            notaToUpdate.setNombre(notaToUpdate.getNombre());
            notaToUpdate.setTitulo(notaToUpdate.getTitulo());
            notaToUpdate.setContenido(notaToUpdate.getContenido());
            notaRepositorio.save(notaToUpdate);
            return "NOTA MODIFICADO";
        }
        return "ERROR AL MODIFICAR";
    }

    @Override
    public Nota updateNote(String nombre, String titulo, String contenido) {
        logger.info("Actualizando un nuevo usuario usando DTO");
        logger.info(nombre);
        logger.info(titulo);
        logger.info(contenido);
        return notaRepositorio.save(new Nota(nombre, titulo, contenido));
    }
}
