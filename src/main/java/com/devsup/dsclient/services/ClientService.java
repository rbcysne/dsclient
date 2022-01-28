package com.devsup.dsclient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsup.dsclient.dto.ClientDTO;
import com.devsup.dsclient.entities.Client;
import com.devsup.dsclient.exceptions.DataBaseException;
import com.devsup.dsclient.exceptions.RegisterNotFoundException;
import com.devsup.dsclient.repositories.ClientRepository;

@Service
public class ClientService {
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ClientRepository clientRepository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(PageRequest pageRequest) {
		
		Page<Client> page;
		
		try {
			page = this.clientRepository.findAll(pageRequest);
		} catch(RegisterNotFoundException e) {
			throw new RegisterNotFoundException(e.getMessage());
		} catch(Exception e) {
			throw new DataBaseException(this.messageSource.getMessage("client-listing-error", null, null));
		}
		
		
		return page.map(ClientDTO::new);
//		return page.map(cli -> new ClientDTO(cli));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		
		Client client = new Client();
		
		Optional<Client> obj = this.clientRepository.findById(id);
		
		client = obj.orElseThrow(() -> new RegisterNotFoundException(this.messageSource.getMessage("register-not-found-with-id", null, null) + " " + id));
		
		return new ClientDTO(client);
	}

	@Transactional
	public ClientDTO insert(ClientDTO clientDto) {

		Client client = new Client();
		
		try {
			this.copyDtoToEntity(clientDto, client);
			this.clientRepository.save(client);
		} catch(Exception e) {
			throw new DataBaseException(e.getMessage());
		}
		
		return new ClientDTO(client);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO clientDto) {
		
		Client client = new Client();
		
		try {
			client = this.clientRepository.getOne(id);
			this.copyDtoToEntity(clientDto, client);
			
			client = this.clientRepository.save(client);
		} catch(EntityNotFoundException e) {
			throw new RegisterNotFoundException(this.messageSource.getMessage("register-not-found-with-id", null, null) + " " + id);
		}
		
		return new ClientDTO(client);
	}
	
	public void delete(Long id) {
		try {
			this.clientRepository.deleteById(id);
		} catch(EmptyResultDataAccessException e) {
			throw new RegisterNotFoundException(this.messageSource.getMessage("register-not-found-with-id", null, null) + " " + id);
		} catch(DataIntegrityViolationException e) {
			throw new DataBaseException(this.messageSource.getMessage("error-deleting", null, null));
		}
	}
	
	
	private void copyDtoToEntity(ClientDTO clientDto, Client client) {
		
		client.setName(clientDto.getName().trim());
		client.setCpf(clientDto.getCpf().trim());
		client.setIncome(clientDto.getIncome());
		client.setBirthDate(clientDto.getBirthDate());
		client.setChildren(clientDto.getChildren());
		
	}

}
