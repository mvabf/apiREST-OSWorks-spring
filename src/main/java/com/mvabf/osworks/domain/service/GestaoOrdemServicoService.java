package com.mvabf.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvabf.osworks.api.model.Comentario;
import com.mvabf.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.mvabf.osworks.domain.exception.NegocioException;
import com.mvabf.osworks.domain.model.Cliente;
import com.mvabf.osworks.domain.model.OrdemServico;
import com.mvabf.osworks.domain.model.StatusOrdemServico;
import com.mvabf.osworks.domain.repository.ClienteRepository;
import com.mvabf.osworks.domain.repository.ComentarioRepository;
import com.mvabf.osworks.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemServico criar(OrdemServico ordemServico) {
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow( () -> new NegocioException("Cliente não encontrado!") );
		
		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordemServicoRepository.save(ordemServico);
	}
	
	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		ordemServico.finalizar();
		
		ordemServicoRepository.save(ordemServico);
	}

	public Comentario adicionarComentario(Long ordemServicoId, String descricao) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		var comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);
		
		return comentarioRepository.save(comentario);
	}
	
	private OrdemServico buscar(Long ordemServicoId) {
		return ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow( () -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada!") );
	}
}
