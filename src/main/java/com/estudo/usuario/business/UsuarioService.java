package com.estudo.usuario.business;

import com.estudo.usuario.business.converter.UsuarioConverter;
import com.estudo.usuario.business.dto.EnderecoDTO;
import com.estudo.usuario.business.dto.TelefoneDTO;
import com.estudo.usuario.business.dto.UsuarioDTO;
import com.estudo.usuario.infrastructure.entity.Endereco;
import com.estudo.usuario.infrastructure.entity.Telefone;
import com.estudo.usuario.infrastructure.entity.Usuario;
import com.estudo.usuario.infrastructure.exceptions.ConflictException;
import com.estudo.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.estudo.usuario.infrastructure.repository.EnderecoRepository;
import com.estudo.usuario.infrastructure.repository.TelefoneRepository;
import com.estudo.usuario.infrastructure.repository.UsuarioRepository;
import com.estudo.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email){
        try {
            boolean existe = verifaEmailExistente(email);
            if(existe){
                throw new ConflictException("email ja existe: " + email);
            }
        }catch (ConflictException e){
            throw new ConflictException("email já cadastrado: ", e.getCause());
        }
    }

    public boolean verifaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        try{
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado: " + email)));

        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado: " + email, e.getCause());
        }
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizarDadosUsuario(String token, UsuarioDTO usuarioDTO) {
        String email = jwtUtil.extrairEmailToken(token.substring(7)); // Remove "Bearer " prefix

        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado: " + email));
        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long id, EnderecoDTO enderecoDTO) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID não encontrado com id: " + id));
        Endereco enderecoAtualizado = usuarioConverter.updateEndereco(enderecoDTO, endereco);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(enderecoAtualizado));
    }

    public TelefoneDTO atualizaTelefone(Long id, TelefoneDTO telefoneDTO) {
        Telefone telefone = telefoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID não encontrado com id: " + id));
        Telefone telefoneAtualizado = usuarioConverter.updateTelefone(telefoneDTO, telefone);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefoneAtualizado));
    }



}


