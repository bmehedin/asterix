import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SmiService {

  private smiUrl: string;

  constructor(private http: HttpClient) {
    this.smiUrl = 'http://localhost:8080/smi';
  }

  public sendSmiData(nbOfPositions: number,
                     nbOfInfringedPositions: number,
                     xPosition: number, yPosition: number,
                     altitude: number) {

    const url = `${this.smiUrl}/${nbOfPositions}/${nbOfInfringedPositions}/${xPosition}/${yPosition}/${altitude}`;

    return this.http.post(url, {}, {
      responseType: 'blob',
      headers: new HttpHeaders({
        'Accept': 'application/zip'
      })
    });
  }
}
