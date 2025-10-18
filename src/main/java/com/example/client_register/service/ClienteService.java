package com.example.client_register.service;

import com.example.client_register.event.ClienteCreadoEvent;
import com.example.client_register.model.Cliente;
import com.example.client_register.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    @Autowired
    private final ClienteRepository clienteRepository;
    private final RabbitTemplate rabbitTemplate;

    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente saveCliente(Cliente cliente) {
        Cliente savedCliente = clienteRepository.save(cliente);
        // Publicar evento en RabbitMQ
        ClienteCreadoEvent event = new ClienteCreadoEvent(
                savedCliente.getId(),
                savedCliente.getNombre(),
                savedCliente.getApellido(),
                savedCliente.getEmail()
        );
        rabbitTemplate.convertAndSend("cliente.exchange", "cliente.creado", event);
        return savedCliente;
    }
}
