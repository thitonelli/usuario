package com.estudo.usuario.business;

import com.estudo.usuario.business.converter.UsuarioConverter;
import com.estudo.usuario.business.dto.UsuarioDTO;
import com.estudo.usuario.infrastructure.entity.Usuario;
import com.estudo.usuario.infrastructure.exceptions.ConflictException;
import com.estudo.usuario.infrastructure.exceptions.ResourceNotFoundException;
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

    public Usuario buscarUsuarioPorEmail(String email){
        return (Usuario) usuarioRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Email não encontrado: " + email));
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



}


