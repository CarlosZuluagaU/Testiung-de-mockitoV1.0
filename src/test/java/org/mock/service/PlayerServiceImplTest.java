package org.mock.service;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mock.DataProvider;
import org.mock.persistence.entity.Player;
import org.mock.repository.PlayerRepositoryImpl;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {

    @Mock
    private PlayerRepositoryImpl playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @BeforeEach
    void init(){
        this.playerRepository= mock(PlayerRepositoryImpl.class);
        this.playerService = new PlayerServiceImpl(this.playerRepository);
    }

    @Test
    public void testFindAll(){
        //Given
        PlayerRepositoryImpl playerRepository = mock(PlayerRepositoryImpl.class);
//        PlayerRepositoryImpl playerRepository = new PlayerRepositoryImpl(); esto crea un nuevo repository
        PlayerServiceImpl playerService = new PlayerServiceImpl(playerRepository);

        //When
        when(playerRepository.findAll()).thenReturn(DataProvider.playerListMock());
        //Mockito trabaja es con la dependencia
        List<Player> result= playerService.findAll();

        //Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Lionel Messi", result.get(0).getName());
    }
    @Test
    public void testFindById(){
        Long id= 1L;
        when(this.playerRepository.findById(1L)).thenReturn(DataProvider.playerMock());
       Player player = this.playerService.findById(1L);
        //THEN
        assertNotNull(player);
        //El verify solo se usa con la dependencia, lo que pasa es que existe la dependencia.
        verify(this.playerRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testSave(){
        //given
        Player player = DataProvider.newPlayerMock();
        //when
        this.playerService.save(player);
        //then

        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(this.playerRepository). save(playerArgumentCaptor.capture());
        assertEquals(12L, playerArgumentCaptor.getValue().getId());
    }

    @Test
    void testDeleteById(){
        Long id = 1L;

        this.playerService.deleteById(id);

        //then
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(this.playerRepository).deleteById(anyLong());
        verify(this.playerRepository).deleteById(longArgumentCaptor.capture());
        assertEquals(1L, longArgumentCaptor.getValue());
    }


}
