package com.personalproject.GestionIncidencias.mapper;

import com.personalproject.GestionIncidencias.dto.response.SolicitudDTOResponse;
import com.personalproject.GestionIncidencias.model.Client;
import com.personalproject.GestionIncidencias.model.ClientSoftware;
import com.personalproject.GestionIncidencias.model.Software;
import com.personalproject.GestionIncidencias.model.Solicitud;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

// Solo carga los dos mappers generados por MapStruct, sin base de datos
@SpringJUnitConfig(classes = {SolicitudMapperImpl.class, ClientMapperImpl.class})
class SolicitudMapperTest {

    @Autowired
    private SolicitudMapper solicitudMapper;

    @Test
    void toDTO_muestraLosSoftwaresDelClienteConSuIdYNombre() {
        Software erp = new Software(10L, "ERP");
        Software crm = new Software(20L, "CRM");

        Client client = new Client();
        client.setId(1L);
        client.setName("ACME");
        client.setSoftwares(new ArrayList<>());
        client.getSoftwares().add(clientSoftware(100L, client, erp));
        client.getSoftwares().add(clientSoftware(200L, client, crm));

        Solicitud solicitud = new Solicitud();
        solicitud.setId(5L);
        solicitud.setClient(client);

        SolicitudDTOResponse dto = solicitudMapper.toDTO(solicitud);

        // Antes salía el id de la tabla intermedia (100, 200) y el nombre en null
        assertThat(dto.getClient().getSoftwares())
                .extracting("id", "name")
                .containsExactly(tuple(10L, "ERP"), tuple(20L, "CRM"));
    }

    private static ClientSoftware clientSoftware(Long id, Client client, Software software) {
        ClientSoftware cs = new ClientSoftware();
        cs.setId(id);
        cs.setClient(client);
        cs.setSoftware(software);
        return cs;
    }
}
