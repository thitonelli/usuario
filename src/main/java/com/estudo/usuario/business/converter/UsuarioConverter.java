package com.estudo.usuario.business.converter;

import com.estudo.usuario.business.dto.EnderecoDTO;
import com.estudo.usuario.business.dto.TelefoneDTO;
import com.estudo.usuario.business.dto.UsuarioDTO;
import com.estudo.usuario.infrastructure.entity.Endereco;
import com.estudo.usuario.infrastructure.entity.Telefone;
import com.estudo.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefone(usuarioDTO.getTelefones()))
                .build();
    }

    public List<Endereco> paraListaEndereco(List<EnderecoDTO>  enderecoDTOS) {
        //UTILIZANDO STREAM (ESTUDAR MAIS)
        //return enderecoDTOS.stream().map(this::paraEndereco).toList();
        List<Endereco> enderecos = new ArrayList<>();
        for (EnderecoDTO enderecoDTO : enderecoDTOS) {
            enderecos.add(paraEndereco(enderecoDTO));
        }
        return enderecos;
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    public List<Telefone> paraListaTelefone(List<TelefoneDTO>  telefoneDTOS) {
        //UTILIZANDO STREAM (ESTUDAR MAIS)
        //return telefonesDTOS.stream().map(this::paraTelefone).toList();
        List<Telefone> telefones = new ArrayList<>();
        for (TelefoneDTO telefoneDTO : telefoneDTOS) {
            telefones.add(paraTelefone(telefoneDTO));
        }
        return telefones;
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    public UsuarioDTO paraUsuarioDTO(Usuario usuarioDTO) {
        return UsuarioDTO.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEnderecoDTO(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefoneDTO(usuarioDTO.getTelefones()))
                .build();
    }

    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco>  enderecoDTOS) {
        //UTILIZANDO STREAM (ESTUDAR MAIS)
        //return enderecoDTOS.stream().map(this::paraEndereco).toList();
        List<EnderecoDTO> enderecos = new ArrayList<>();
        for (Endereco enderecoDTO : enderecoDTOS) {
            enderecos.add(paraEnderecoDTO(enderecoDTO));
        }
        return enderecos;
    }

    public EnderecoDTO paraEnderecoDTO(Endereco enderecoDTO) {
        return EnderecoDTO.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .cep(enderecoDTO.getCep())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .build();
    }

    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone>  telefoneDTOS) {
        //UTILIZANDO STREAM (ESTUDAR MAIS)
        //return telefonesDTOS.stream().map(this::paraTelefone).toList();
        List<TelefoneDTO> telefones = new ArrayList<>();
        for (Telefone telefoneDTO : telefoneDTOS) {
            telefones.add(paraTelefone(telefoneDTO));
        }
        return telefones;
    }

    public TelefoneDTO paraTelefone(Telefone telefoneDTO) {
        return TelefoneDTO.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario usuario) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : usuario.getNome())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : usuario.getEmail())
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : usuario.getSenha())
                .enderecos(usuario.getEnderecos() != null ? new ArrayList<>(usuario.getEnderecos()) : null)
                .telefones(usuario.getTelefones() != null ? new ArrayList<>(usuario.getTelefones()) : null)
                .build();
    }


}
