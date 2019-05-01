/*
 * Copyright (c) 2015-2019, Virgil Security, Inc.
 *
 * Lead Maintainer: Virgil Security Inc. <support@virgilsecurity.com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     (1) Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *
 *     (2) Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *     (3) Neither the name of virgil nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.virgilsecurity.pythia.crypto;

import java.util.Random;

import com.virgilsecurity.crypto.pythia.Pythia;
import com.virgilsecurity.crypto.pythia.PythiaBlindResult;
import com.virgilsecurity.sdk.crypto.VirgilCrypto;
import com.virgilsecurity.sdk.crypto.VirgilKeyPair;
import com.virgilsecurity.sdk.crypto.exceptions.CryptoException;
import com.virgilsecurity.sdk.utils.ConvertionUtils;

/**
 * Virgil implementation of all crypto operation needed by Pythia.
 * 
 * @author Andrii Iakovenko
 *
 */
public class VirgilPythiaCrypto implements PythiaCrypto {

  private VirgilCrypto virgilCrypto;
  private Random random;

  /**
   * Create a new instance of {@link VirgilPythiaCrypto}.
   *
   */
  public VirgilPythiaCrypto() {
    this.virgilCrypto = new VirgilCrypto();
    this.random = new Random();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.virgilsecurity.pythia.crypto.PythiaCrypto#blind(java.lang.String)
   */
  @Override
  public BlindResult blind(String password) {
    PythiaBlindResult blindResult = Pythia.blind(ConvertionUtils.toBytes(password));
    return new BlindResult(blindResult.blindedPassword, blindResult.blindingSecret);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.virgilsecurity.pythia.crypto.PythiaCrypto#deblind(byte[], byte[])
   */
  @Override
  public byte[] deblind(byte[] transformedPassword, byte[] blindingSecret) {
    return Pythia.deblind(transformedPassword, blindingSecret);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.virgilsecurity.pythia.crypto.PythiaCrypto#verify(byte[], byte[], byte[], byte[],
   * byte[], byte[])
   */
  @Override
  public boolean verify(byte[] transformedPassword, byte[] blindedPassword, byte[] tweak,
      byte[] transformationPublicKey, byte[] proofC, byte[] proofU) {
    try {
      Pythia.verify(transformedPassword, blindedPassword, tweak,
                    transformationPublicKey, proofC, proofU);
      return true;
    } catch (Throwable throwable) {
      return false; // TODO change when fixed in crypto
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.virgilsecurity.pythia.crypto.PythiaCrypto#updateDeblinded(byte[], byte[])
   */
  @Override
  public byte[] updateDeblinded(byte[] deblindedPassword, byte[] updateToken) {
    return Pythia.updateDeblindedWithToken(deblindedPassword, updateToken);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.virgilsecurity.pythia.crypto.PythiaCrypto#generateSalt()
   */
  @Override
  public byte[] generateSalt() {
    byte[] rndBytes = new byte[32];
    random.nextBytes(rndBytes);
    return rndBytes;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.virgilsecurity.pythia.crypto.PythiaCrypto#generateKeyPair(com.virgilsecurity.sdk.crypto.
   * KeysType, byte[])
   */
  @Override
  public VirgilKeyPair generateKeyPair(byte[] seed) throws CryptoException {
    return this.virgilCrypto.generateKeyPair(seed);
  }
}
