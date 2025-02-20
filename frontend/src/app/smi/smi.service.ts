import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

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

    const url = `${this.smiUrl}/send-data/${nbOfPositions}/${nbOfInfringedPositions}/${xPosition}/${yPosition}/${altitude}`;
    return this.http.post<void>(url, {})
  }
}
